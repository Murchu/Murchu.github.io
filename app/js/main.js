document.onreadystatechange = function () {


// fade in divs
var fadein = $('div.fadein');
$.each(fadein, function(i, item) {
	setTimeout(function() {
		$(item).fadeIn(1000); // duration of fadein
	}, 1000 * i); // duration between fadeins
});​


	enquire.register("screen and (min-width: 951px)", { 

		match : function() {
			 $(".drop").show();
		},
		unmatch :function(){	    
			 $(".drop").hide();
		} 
	});

	enquire.register("screen and (min-width: 800px)", { 

		match : function() {
			 $(".target").show();
		},
		unmatch :function(){	    
			 $(".target").hide();
		} 
	});
	
	
	 var	menuIcon = document.getElementById( 'menu-icon' ),
			menuRight = document.getElementById( 'menu' ),
			showRightPush = document.getElementById( 'showRightPush' ),
			body = document.body;
	

	menuIcon.onclick = function() {
			classie.toggle( this, 'active' );
			classie.toggle( body, 'menu-push-toleft' );
			classie.toggle( menuRight, 'menu-open' );
			classie.toggle( wrapper,"dim");	
	};

	$(window).on("scroll", function() {
	    if($(window).scrollTop() > 50) {
	        $(".clearfix").addClass("active");
	        $(".clearfix").addClass("active");
	    } else {
	       $(".clearfix").removeClass("active");      
	    }
	});


/*-------------------------------------------------------------------------------------------------------------*/
  

	  $(function(){
	    $(window).scroll(function() { 
	        if ($(this).scrollTop() > 100) { 
				$("#murchu:hidden").css('visibility','visible');   
				$("#murchu:hidden").fadeIn("fast"); 
				$("#home").css('color','#41769a'); 
				$("#menu-icon").css('color','#41769a');
	        } 
	        else {     
				$("#murchu:visible").fadeOut("slow"); 
				$("#home").css('color','white');
				$("#menu-icon").css('color','white');
	        }  
	    });
	});  



/*-------------------------------------------------------------------------------------------------------------*/


	$(function() {

	    var parallax = document.querySelectorAll(".parallax"),
	        speed = 0.5;

	    window.onscroll = function() {
	        [].slice.call(parallax).forEach(function(el, i) {

	            var windowYOffset = window.pageYOffset,
	                elBackgrounPos = "50% " + (windowYOffset * speed) + "px";

	            el.style.backgroundPosition = elBackgrounPos;

	        });
	    };

	})();

/*-------------------------------------------------------------------------------------------------------------*/


}// end


