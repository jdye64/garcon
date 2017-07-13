
/**
Gets all of the devices that are currently in the registry
*/
function getAllDevices() {
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
    $.get("/api/v1/c2/device/2597535544847920681", function(data) {
        console.log(JSON.stringfy(data));
    }, "json");
}

//onload invocations.
getDevice();
getDeviceOperations();  //History of current Device Operations.