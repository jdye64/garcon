

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

/**
Gets all of the devices that are currently in the registry
*/
function getDevice() {
	$.get("/api/v1/c2/device", function(data) {
	    console.log(JSON.stringify(data));
	    for(var i = 0; i < data.length; i++) {
	        var obj = data[i];
	        console.log(JSON.stringify(obj));
	        $("#contentBody")
	            .append("<tr><td align='center'><a href='device.html?deviceId=" + obj.NetworkInfo.deviceid + "'>" + obj.NetworkInfo.deviceid + "</a></td>"
	            + "<td align='center'>" + obj.NetworkInfo.ip + "</td>"
	            + "<td align='center'>" + obj.NetworkInfo.hostname + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.machinearch + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.vcores + "</td>"
	            + "<td align='center'>" + formatBytes(obj.SystemInformation.physicalmem, 1) + "</td>"
	            + "</tr>");
	    }

	}, "json");
}

function setOperationsValues() {
    console.log("Setting opeartion values");
    $.get("/api/v1/c2/operations/supported", function(data) {
        $.each(data.supportedOperations, function(i, option) {
            console.log("Option: " + option);
            console.log("I: " + i);
            $('#operationsSel').append($('<option/>').attr("value", option).text(option));
        });
    }, "json");
}

function getDeviceOperations() {
    console.log("listing device operations history");
    var deviceId = getUrlParameter("deviceId");
    $.get("/api/v1/c2/device/" + deviceId + "/operations", function(data) {
        console.log(JSON.stringify(data));
        $("#operationHistoryBody").empty();
        for(var i = 0; i < data.length; i++) {
            var obj = data[i];

            $("#operationHistoryBody")
        	    .append("<tr><td align='center'>" + obj.operationid + "</td>"
        	            + "<td align='center'>" + obj.operation + "</td>"
        	            + "<td align='center'>" + obj.name + "</td>"
        	            + "<td align='center'>" + obj.acked + "</td>"
        	            + "<td align='center'>" + obj.ackedTimestamp + "</td>"
        	            + "</tr>");
        }
        $("#leoperationtable").DataTable();
    }, "json");
}


function getDeviceInformation() {
    console.log("listing device queue Metrics");
     var deviceId = getUrlParameter("deviceId");
        $.get("/api/v1/c2/device/" + deviceId + "/information", function(data) {
            console.log(JSON.stringify(data));
            $("#deviceInfo").empty();
            for(var i = 0; i < data.length; i++) {
                var obj = data[i];
                $("#deviceInfo")
            	    .append("<tr><td align='center'>" + obj.SystemInformation.machinearch + "</td>"
            	            + "<td align='center'>" + formatBytes(obj.SystemInformation.physicalmem, 1) + "</td>"
            	            + "</tr>");
            }
        }, "json");
    $.get("/api/v1/c2/device/" + deviceId + "/connections", function(data) {
        console.log(JSON.stringify(data));
        $("#connectMetrics").empty();
        for(var i = 0; i < data.length; i++) {
            var obj = data[i];

            $("#connectMetrics")
        	    .append("<tr><td align='center'>" + obj.queue_metrics_id + "</td>"
        	            + "<td align='center'>" + obj.datasizemax + "</td>"
        	            + "<td align='center'>" + obj.datasize + "</td>"
        	            + "<td align='center'>" + obj.queued + "</td>"
        	            + "<td align='center'>" + obj.queuedmax + "</td>"
        	            + "</tr>");
        }
    }, "json");
}


//onload invocations.
//getDevice();
setOperationsValues();
getDeviceOperations();  //History of current Device Operations.
getDeviceInformation();

window.setInterval(getDeviceOperations(), 1000);
window.setInterval(getDeviceInformation, 1000);



// Listen for the operation creation click
$("#createOperation").click(function() {
    var operation = $("#operationsSel option:selected").text();
    var contentSel = $("#contentoptions option:selected").text();
    var name = $("#operationName").val();
    var deviceId = getUrlParameter("deviceId");
    var json = {};
    json.operation = operation;
    switch(operation){
    case "START":
    case "start":
            console.log("start");
            json.name = contentSel;
            break;
    case "STOP":
    case "stop":
        console.log("stop");
        json.name = contentSel;
        break;
    case "CLEAR":
    case "clear":
        console.log("clearing connection");
        json.name = "connection";
        if(contentSel.length > 0){
                json.content = new Object();
                json.content["connection"] = contentSel;
            }
        break;
    default:
        json.name = "";
    break;
    }



    json.deviceId = deviceId;

    var jsonString = JSON.stringify(json);
    console.log("Sending " + jsonString);
    $.ajax({
      url:"/api/v1/c2/device/operation",
      type:"POST",
      data:jsonString,
      contentType:"application/json",
      dataType:"json",
      success: function(data){
        console.log("Data: " + JSON.stringify(data));
        getDeviceOperations();
      }
    });
});



$("#operationsSel").ready(function () {

  $('#contentoptions').hide();
  $('#operationsSel').change(function () {
    var operation = $("#operationsSel option:selected").text();
    if (operation == "CLEAR" )
    {
    var options = $("#contentoptions");
    options.empty();
    var deviceId = getUrlParameter("deviceId");
        $.get("/api/v1/c2/device/" + deviceId + "/connections", function(data) {
            console.log(JSON.stringify(data));
            for(var i = 0; i < data.length; i++) {
                var obj = data[i];
                        options.append($("<option />").val(obj.queueMetricsId).text(obj.queueMetricsId));

            }
            $('#contentoptions').show();
        }, "json");

    }
    else if (operation == "STOP"){

     var options = $("#contentoptions");
        options.empty();
        var deviceId = getUrlParameter("deviceId");
            $.get("/api/v1/c2/device/" + deviceId + "/components", function(data) {
                console.log(JSON.stringify(data));
                for(var i = 0; i < data.length; i++) {
                    var obj = data[i];
                    if (obj.Status == true){
                        options.append($("<option />").val(obj.Component).text(obj.Component));
                    }

                }
                $('#contentoptions').show();
            }, "json");
    }
    else if (operation == "START"){

         var options = $("#contentoptions");
            options.empty();
            var deviceId = getUrlParameter("deviceId");
                $.get("/api/v1/c2/device/" + deviceId + "/components", function(data) {
                    console.log(JSON.stringify(data));
                    for(var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        if (obj.Status == false){
                            options.append($("<option />").val(obj.Component).text(obj.Component));
                        }

                    }
                    $('#contentoptions').show();
                }, "json");
        }
     else
  $('#contentoptions').hide();
  })
});