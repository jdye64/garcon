
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
	    console.log(JSON.stringify(data));
	    for(var i = 0; i < data.length; i++) {
	        var obj = data[i];
	        console.log(JSON.stringify(obj));
	        $("#contentBody")
	            .append("<tr><td align='center'>" + obj.NetworkInfo.deviceid + "</td>"
	            + "<td align='center'>" + obj.NetworkInfo.ip + "</td>"
	            + "<td align='center'>" + obj.NetworkInfo.hostname + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.machinearch + "</td>"
	            + "<td align='center'>" + obj.SystemInformation.vcores + "</td>"
	            + "<td align='center'>" + formatBytes(obj.SystemInformation.physicalmem, 1) + "</td>"
	            + "</tr>");
	    }

	}, "json");
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getMiNiFiHUD() {
	console.log("Retrieving the HUD for all processors");
	$.get("/api/v1/c2/hud", function(data) {
	    console.log(JSON.stringify(data));
	    var devicesInfo = "Total: " + data["totalDevices"] + " / Running: " + data["runningDevices"] + " / Stopped: " + data["stoppedDevices"]
	    $("#minifiDevicesHUD").append(devicesInfo);
	}, "json");
}

//onload invocations.
getMiNiFiHUD();
getAllDevices();