 $(document).ready(function(){
	$('.carousel-full').owlCarousel({
    loop: true,
    margin: 0,
    nav: false,
    items: 1,
    dots: false,
//    dots: true,
	});
	
	$('.carousel-single').owlCarousel({
	    stagePadding: 30,
	    loop: true,
	    margin: 16,
	    nav: false,
	    dots: false,
	    responsiveClass: true,
	    responsive: {
	        0: {
	            items: 1,
	        },
	        768: {
	            items: 3,
	        }
	    }
	
	});
	$('.carousel-multiple').owlCarousel({
	    stagePadding: 32,
	    loop: true,
	    margin: 16,
	    nav: false,
	    items: 2,
	    dots: false,
	    responsiveClass: true,
	    responsive: {
	        0: {
	            items: 2,
	        },
	        768: {
	            items: 4,
	        }
	    }
	});
	$('.carousel-small').owlCarousel({
	    stagePadding: 32,
	    loop: true,
	    margin: 16,
	    nav: false,
	    items: 5,
	    dots: false,
	    responsiveClass: true,
	    responsive: {
	        0: {
	            items: 5,
	        },
	        768: {
	            items: 8,
	        }
	    }
	});
	$('.carousel-slider').owlCarousel({
	    loop: true,
	    margin: 8,
	    nav: false,
	    items: 1,
	    dots: true,
	});
	$('.story-blocks').owlCarousel({
	    stagePadding: 32,
	    loop: false,
	    margin: 16,
	    nav: false,
	    items: 5,
	    dots: false,
	    responsiveClass: true,
	    responsive: {
	        0: {
	            items: 5,
	        },
	        768: {
	            items: 8,
	        }
	    }
	});
});