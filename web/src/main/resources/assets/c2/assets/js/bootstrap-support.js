$(document).ready(function () {

	$(".nav a").on("click", function(e){
	   $(".nav").find(".active").removeClass("active");
	   $(this).parent().addClass("active");

	   e.preventDefault();
	   $('.content').hide();
	   var ref = $(this).attr('href');
	   console.log("HREF: " + ref);
	   console.log("Element: " + JSON.stringify($('#' + $(this).attr('href'))));
	   $('#' + $(this).attr('href')).show();
	});
});