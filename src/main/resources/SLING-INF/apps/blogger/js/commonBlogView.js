
     $(document).ready(function(){
    	  $(".sub-menu-image").click(function(){
    	    $(".pop-blog-menu").slideToggle();
    	  });
    	  
    	  $("nav.main-menu > a").click(function(){
    	    $("#bg-mask").show("fast");
    	    $(".popup-container").slideToggle();
    	  });

    	$(".close-popup").click(function(){
    	    $("#bg-mask").hide();
    	    $(".popup-container").hide();
    	  });
    	  
    	  $(".popup-blog-name > a").click(function(){
    	    $(".popup-menu").slideDown();
    	  });
    	  $(".post-menu-item > a").click(function(){
    	    $(".popup-menu").slideUp();
    	  });
    	
  		$('.custmize, .cancel-btn').click(function() {
  			$('.theme-bar').animate({
  				width : 'toggle'
  			}, 350);
  		  $(".content").mCustomScrollbar({
    			scrollButtons : {
    				enable : true
    			}
    		});

  		});
    	/*  var winHeight = document.documentElement.clientHeight;
    		     $('.main-container').css('height', winHeight-0);*/
  		$(".csstheme ul li a img").click(function(){
  		     $(".csstheme ul li a img").addClass("gray");
  		     $(this).removeClass("gray");
  		      $(".bgTheme ul li a img").removeClass("gray");

  		   });
  		   $(".bgTheme ul li a img").click(function(){
  		     $(".bgTheme ul li a img").addClass("gray");
  		     $(this).removeClass("gray");
  		     $(".csstheme ul li a img").removeClass("gray")
  		});	     
    		        
    	});
  
    	function showVideo(obj) 
    	{
    	 $(obj).hide();
    	  $(obj).prev().hide();
    	  $(obj).next('div').html($(obj).parent().parent().prev().val());
    	} 

    	var imageObject;
    	    function imageFull(obj){
    	    
    	         $("#fullImageViewId").attr('src',"");
    	         $("#bg-mask").show();
    	         imageObject = obj;
    	         $("#fullImageViewId").attr('src',$(obj).children('img').attr("src").replace("x320","xOp"));
    	         $(".full-image-popup").show();
    	      }
    	    
    	    function nextImage(){
    	        if($(imageObject).next().length>0){
    	            imageObject = $(imageObject).next();
    	        }else{
    	            imageObject = $(imageObject).parent().children("a:first-child");
    	        }
    	        $("#fullImageViewId").attr('src',$(imageObject).children('img').attr("src").replace("x320","xOp"));
    	    }
    	    
    	    function previousImage(){
    	        if($(imageObject).prev().length>0){
    	            imageObject = $(imageObject).prev();
    	        }else{
    	            imageObject = $(imageObject).parent().children("a:last-child");
    	        }
    	        $("#fullImageViewId").attr('src',$(imageObject).children('img').attr("src").replace("x320","xOp"));
    	    }
    	 $(function(){
    		$(document.documentElement).keyup(function (e) {
    	        if($(".full-image-popup").css('display') != 'none'){
    	            if (e.keyCode == 39){        
    	               nextImage();
    	            }
    	            if (e.keyCode == 37){
    	                previousImage();
    	            }
    	        }
    	    }); 
    		$("body").click(function(e){
    	      if(e.target.className !== "search-menu"){
    	        $(".search-menu").hide();
    	      }    
    	      if(e.target.className !== "sub-menu-image"){
    	        $(".pop-blog-menu").hide();
    	      }
    	      if(e.target.id=="bg-mask" && $(".full-image-popup").css('display') != 'none' 
    	              && e.target.className !== "full-image-popup"){
    	    	  $("#fullImageViewId").attr('src','');
    	          $("#bg-mask").hide();
    	          $(".full-image-popup").hide();
    	      }
    	  });
    	  $(".close-image").click(function(){
    	      $("#fullImageViewId").attr('src','');
    	      $("#bg-mask").hide();
    	      $(".full-image-popup").hide();
    	  });
    	});
    	
    	 
    	 
    	   function readURL(input) {
    	  
    	    	if (input.files && input.files[0]) {
    	    		var reader = new FileReader();
    	    		reader.onload = function(e) {
    	    			$('#imageId').attr('src', e.target.result).width(24).height(24);
    	    			document.body.background = e.target.result;
    	    			
    	    		};
    	    		reader.readAsDataURL(input.files[0]);
    	    	}
    	    }
    	    
    	 