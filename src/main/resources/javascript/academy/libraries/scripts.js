var $body   = $(document.body);
var navHeight = $('.navbar').outerHeight(true) - 80;

$body.scrollspy({
	target: '#left-toc-sidebar',
	offset: navHeight
});


$(document).ready(function(){

	
	//TAKE THE TALLEST DOC HEIGHT
	function setdocheight() {	
	
		if($(window).width() > 998){
		
			var max = -1;
			
			$(".doc-height").css("height", "auto");
								
			$(".doc-height").each(function() {
			    var h = $(this).height(); 
			    max = h > max ? h : max;
			});
					
			$(".doc-height").css("height", max + 50 , "px");
							
		} else {
			$(".doc-height").css("height", "auto");
        }
    }
    // FUNCTION CALLS
	
	// on page load
	setdocheight();
	
 	
	// on resize
	window.onresize = function(event) {
		setdocheight();
 	};	
	
	// on scroll
	$(window).scroll(function(){
	});



});