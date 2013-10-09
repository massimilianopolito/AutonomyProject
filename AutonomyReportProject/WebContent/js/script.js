$(document).ready(function() {
	
	
	$.vegas('slideshow', {
		delay:10000,
		fade:1000,
		backgrounds:[
			{ src:'../img/slide/img-1-blue.jpg' },
			{ src:'../img/slide/img-2-blue.jpg' },
			{ src:'../img/slide/img-3-blue.jpg' },
			{ src:'../img/slide/img-4-blue.jpg' }
		]
	});
	//('overlay', {
	//	src:'img/overlays/04.png',
	//	opacity:1
	//});

	// clock
	// Create two variable with the names of the months and days in an array
	var monthNames = [ "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" ]; 
	var dayNames= ["Dom.","Lun.","Mar.","Mer.","Gio.","Ven.","Sab."]

	// Create a newDate() object
	var newDate = new Date();
	// Extract the current date from Date object
	newDate.setDate(newDate.getDate());
	// Output the day, date, month and year    
	$('#Date').html(dayNames[newDate.getDay()] + " " + newDate.getDate() + ' ' + monthNames[newDate.getMonth()] + ' ' + newDate.getFullYear());

	setInterval( function() {
		// Create a newDate() object and extract the seconds of the current time on the visitor's
		var seconds = new Date().getSeconds();
		// Add a leading zero to seconds value
		$("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
	},1000);
	
	setInterval( function() {
		// Create a newDate() object and extract the minutes of the current time on the visitor's
		var minutes = new Date().getMinutes();
		// Add a leading zero to the minutes value
		$("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
    },1000);
	
	setInterval( function() {
		// Create a newDate() object and extract the hours of the current time on the visitor's
		var hours = new Date().getHours();
		// Add a leading zero to the hours value
		$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
    }, 1000);
	
});
