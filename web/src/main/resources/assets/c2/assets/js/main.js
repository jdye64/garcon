console.log("Main.js is working");

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
	console.log("Retrieving all devices in the registry");
	$.get("/api/v1/device", function(data) {
	    console.log(JSON.stringify(data));
	    for(var i = 0; i < data.length; i++) {
	        var obj = data[i];
	        console.log("Device: " + obj);
	        $("#contentBody")
	            .append("<tr><td align='center'><a href='operations/index.html'>" + obj.primaryNICMac + "</a></td>"
	            + "<td align='center'>" + obj.externalIPAddress + "</td>"
	            + "<td align='center'>" + obj.hostname + "</td>"
	            + "<td align='center'>" + obj.availableProcessors + "</td>"
	            + "<td align='center'>" + formatBytes(obj.jvmFreeMemory, 1) + "</td>"
	            + "<td align='center'>" + formatBytes(obj.jvmTotalMemory, 1) + "</td>"
	            + "<td align='center'>" + formatBytes(obj.rootDiskReport.availableBytes, 1) + "</td>"
	            + "<td align='center'>" + formatBytes(obj.rootDiskReport.totalBytes, 1) + "</td>"
	            + "</tr>");
	    }

	}, "json");
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getPressuredConnections() {
	console.log("Retrieving the pressured connections");
	$.get("/api/v1/connection", function(data) {
	    console.log(JSON.stringify(data));
	    for(var i = 0; i < data.length; i++) {
	        var obj = data[i];
	        console.log("Pressured Connection: " + obj);
	        $("#pressuredConnectionBody")
	            .append("<tr><td align='center'>" + obj.id + "</td>"
	            + "<td align='center'>" + obj.backPressureObjectThreshold + "</td>"
	            + "<td align='center'>" + obj.backPressureDataSizeThreshold + "</td>"
	            + "<td align='center'>" + obj.queuedCount + "</td>"
	            + "<td align='center'>" + obj.queuedBytes + "</td>"
	            + "</tr>");
	    }

	}, "json");
}

function getProcessorsHUD() {
	console.log("Retrieving the HUD for all processors");
	$.get("/api/v1/processors/hud", function(data) {
	    console.log(JSON.stringify(data));
	    $("#totalNumProcessors").append(numberWithCommas(data["totalNumProcessors"]));
	    $("#numStoppedProcessors").append(numberWithCommas(data["numStoppedProcessors"]));
		$("#numInvalidProcessors").append(numberWithCommas(data["numInvalidProcessors"]));
		$("#numDisabledProcessors").append(numberWithCommas(data["numDisabledProcessors"]));
		$("#numRunningProcessors").append(numberWithCommas(data["numRunningProcessors"]));
	}, "json");
}

/** 
Get the high level HUD or dashboard metrics for the registry connections
*/
function getConnectionsHUD() {
	console.log("Retrieving connections HUD");
	$.get("/api/v1/connection/hud", function(data) {
	    console.log(JSON.stringify(data));
	    $("#backPressuredConnections").append(data["backPressuredConnections"] + "/" + data["totalConnections"]);
	    $("#backPressuredBytes").append(formatBytes(data["backPressuredBytes"], 1));
	    $("#backPressuredObjects").append(numberWithCommas(data["backPressuredObjects"]));
	}, "json");
}

//onload invocations.
getAllDevices();
getProcessorsHUD();
getPressuredConnections();
getConnectionsHUD();