$(document).ready(function() {
	
	
	$.vegas('slideshow', {
		delay:5000,
		fade:2000,
		backgrounds:[
			{ src:'../img/slide/img-(1).jpg' },
			{ src:'../img/slide/img-(2).jpg' },
			{ src:'../img/slide/img-(3).jpg' },
			{ src:'../img/slide/img-(4).jpg' },
			{ src:'../img/slide/img-(5).jpg' }
		]
	})('overlay', {
		src:'../img/overlays/04.png',
		opacity:0.7
	});

	
	
});
