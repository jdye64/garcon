

var selectedDeviceId="";
var selectedDeviceRecentOps = {};

var c2ops = {};
c2ops["HeartBeat Period"] = "c2.agent.heartbeat.period";

var flowOps = {};
flowOps["HeartBeat Period"] = "c2.agent.heartbeat.period";


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

     if (selectedDeviceId.length == 0){
        $("#deviceOps").empty();
        return;
     }

    $.get("/api/v1/c2/device/" + selectedDeviceId + "/operations", function(data) {
        
        $("#deviceOps").empty();
        // grab last five
        var j =0;
        var idsToFlash=[];
        var firstRun=true;
        for(var items in selectedDeviceRecentOps){
            firstRun = false;
            break;
        }
        for(var i = data.length-1; i >= data.length - 5; i--, j++) {
            var obj = data[i];
            var status = "RUNNING";
            if (selectedDeviceRecentOps[j] != obj.acked)
            {
                idsToFlash.push( "status" + j);
            }
            if (obj.acked == true){
                status = "FINISHED";
            }
            selectedDeviceRecentOps[j] = obj.acked;

            $("#deviceOps")
        	    .append("<tr><td align='left'>" + obj.operation + "</td>"
        	            + "<td id ='status" + j + "' align='left'>" + status + "</td>"
        	            + "</tr>");
        }



        for( var idToFlash in idsToFlash){
            setTimeout(function() {
                if (selectedDeviceRecentOps[idToFlash] == false)
                $("#status" + idToFlash ).css("background-color", "yellow");
                else
                $("#status" + idToFlash ).css("background-color", "green");
            }, 100);

        }
    }, "json");
}


function getDeviceInformation() {

     if (selectedDeviceId.length == 0){
        $("#deviceOps").empty();
        $("#connectMetrics").empty();
        return;
     }

     getDeviceOperations();

    $.get("/api/v1/c2/device/" + selectedDeviceId + "/connections", function(data) {
        console.log(JSON.stringify(data));
        $("#connectMetrics").empty();
        for(var i = 0; i < data.length; i++) {
            var obj = data[i];

            $("#connectMetrics")
        	    .append("<tr><td align='left'>" + obj.queue_metrics_id + "</td>"
        	            + "<td align='center'>" + obj.datasize + "</td>"
        	            + "<td align='center'>" + obj.queued + "</td>"
        	            + "</tr>");
        }
    }, "json");
}


//onload invocations.
//getDevice();
setOperationsValues();
getDeviceInformation();


window.setInterval(getDeviceInformation, 1000);



// Listen for the operation creation click
$("#createOperation").click(function() {
    var operation = $("#operationsSel option:selected").text();
    var contentSel = $("#contentoptions option:selected").text();
    var name = $("#operationName").val();
    var deviceId = selectedDeviceId;
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
    case "UPDATE":
    case "update":
            console.log("stop");
            var option = $("#option option:selected").val();
            var value = $("#value").val();
            if (contentSel == "C2 Agent"){
                json.name = "c2";
                json.content = new Object();
                                json.content[option] = value;
            }else{
                json.name = "configuration";
                json.content = new Object();
                $.ajax({
                     async: false,
                     type: 'GET',
                     url: "/api/v1/c2/device/config/" + selectedDeviceId ,
                     success: function(data) {

                          console.log(JSON.stringify(data));
                          var obj = data;

                              var addy = location.protocol + "//" + location.host;
                              //if (location.port.length > 0 && ){
                                //  addy = addy + ":" + location.port;
                              //}
                              addy = addy + "/api/v1/c2/device/configfile/" + obj.deviceConfigId;
                              json.content["location"] = addy;

                     }
                });


            }


            break;
    case "DESCRIBE":
                var contentOptionValue = $("#contentoptions option:selected").val();
                if (contentSel == "Processor Metrics"){
                    json.name = "metrics";
                    json.content = new Object();
                    json.content["metricsClass"] = contentOptionValue;
                }
                else if (contentSel == "Configuration"){
                                         json.name = "configuration";
                                         json.content = new Object();

                                     }




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
  $('#option').hide();
  $('#value').hide();

  $('#operationsSel').change(function () {
  var operations = $("#option");
    $('#option').hide();
    $('#value').hide();
    $('#contentoptions').hide();
    operations.hide();
    var operation = $("#operationsSel option:selected").text();
    if (operation == "CLEAR" )
    {
    var options = $("#contentoptions");
    options.empty();
    var deviceId = selectedDeviceId;
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
        var deviceId = selectedDeviceId;
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
            var deviceId = selectedDeviceId;
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
     else if (operation == "UPDATE"){

        var options = $("#contentoptions");
        options.empty();
        options.append($("<option />").val("FlowFile").text("FlowFile"));
        options.append($("<option />").val("C2 Agent").text("C2 Agent"));

        options.show();
        operations.hide();
        $('#value').hide();
        operations.empty();




        }
        else if (operation == "DESCRIBE"){

                var options = $("#contentoptions");
                options.empty();
                options.append($("<option />").val("15").text("Processor Metrics"));
                options.append($("<option />").val("1").text("Configuration"));

                options.show();
                operations.hide();
                $('#value').hide();
                operations.empty();




                }
     else{
     $('#option').hide();
         $('#value').hide();
            $('#contentoptions').hide();
    }

     $('#contentoptions').change(function () {
            if (operation == "UPDATE"){
                var contentSel = $("#contentoptions option:selected").text();
                if (contentSel == "C2 Agent"){
                    for ( var key in c2ops){
                        operations.append($("<option />").val(c2ops[key]).text(key));
                    }
                }
                console.log("showing");
                operations.show();
                $('#value').show();
                }
                else{
                console.log("hiding");
                    operations.hide();
                    $('#value').hide();
                                }
            });
  })
});
///

function formatBytes(bytes, decimals) {
   if(bytes == 0) return '0 Bytes';
   var k = 1000,
       dm = decimals + 1 || 3,
       sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
       i = Math.floor(Math.log(bytes) / Math.log(k));
   return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

/**
Gets all of the devices that are currently in the registry
*/
function getAllDevices() {
	$.get("/api/v1/c2/device", function(data) {
	    for(var i = 0; i < data.length; i++) {
	        var obj = data[i];
	        $("#contentBody")
	            .append("<tr><td align='center'>" + obj.NetworkInfo.deviceid + "</a></td>"
	            + "<td align='center'>" + obj.NetworkInfo.ip + "</td>"
	            + "<td align='center'>" + obj.NetworkInfo.hostname + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.machinearch + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.vcores + "</td>"
	            + "<td align='center'>" + formatBytes(obj.SystemInformation.physicalmem, 1) + "</td>"
	            + "</tr>");
	    }
    var table = $('#deviceTable').DataTable();


    $('#deviceTable tbody').on( 'click', 'tr', function () {
            if ( $(this).hasClass('selected') ) {
                $(this).removeClass('selected');
            }
            else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
            var data = table.row( this ).data();
            selectedDeviceId=data[0];
            getDeviceOperations();  //History of current Device Operations.
            getDeviceInformation();
            $('#minifiDevicesHUDSelection').empty();
            $('#minifiDevicesHUDSelection').append("Showing " + selectedDeviceId);
            console.log( 'You clicked on '+selectedDeviceId+'\'s row' );
        } );

        $('#deviceTable tbody tr:eq(0)').click();
	}, "json");
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getMiNiFiHUD() {
	console.log("Retrieving the HUD for all processors");
	$.get("/api/v1/c2/hud", function(data) {
	    
	    var devicesInfo = "Total: " + data["totalDevices"] + " / Running: " + data["runningDevices"] + " / Stopped: " + data["stoppedDevices"]
	    $("#minifiDevicesHUD").append(devicesInfo);
	}, "json");
}

function createTables(){
    console.log("Creating metrics");
    $('#metrics').DataTable({searching: false, paging: false, "bInfo" : false});
    $('#opus').DataTable({searching: false, paging: false, "bInfo" : false});
}
//onload invocations.
getMiNiFiHUD();
getAllDevices();
createTables();