
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

function getDeviceOperations() {
    console.log("listing device operations history");
    var deviceId = getUrlParameter("deviceId");
    $.get("/api/v1/c2/device/" + deviceId + "/operations", function(data) {
        console.log(JSON.stringify(data));
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
    }, "json");
}

//onload invocations.
//getDevice();
getDeviceOperations();  //History of current Device Operations.