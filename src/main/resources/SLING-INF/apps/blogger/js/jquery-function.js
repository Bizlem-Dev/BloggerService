var contextPath = window.location.pathname.split("/")[1];
$(document).ready(function(){
  $(".sub-menu-image").click(function(){
    $(".pop-blog-menu").slideToggle();
  });
 
$("nav.main-menu > a").click(function(){
	$("#bg-mask").show();
});

$(".close-popup").click(function(){
    $("#bg-mask").hide();
    $(".popup-container").slideUp();
    $(".post").removeClass("popUpHide");
});

$(".text").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewText").slideDown();
	$(".main-menu").hide();
	//$("#text-popup").slideDown();
});

$(".photo").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewImage").slideDown();
	$(".main-menu").hide();
});

$(".quote").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewQuote").slideDown();
	$(".main-menu").hide();
});

$(".link").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewLink").slideDown();
	$(".main-menu").hide();
});

$(".chat").parent("a").click(function(){
	$("#text-popup").slideDown();
});

$(".audio").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewAudio").slideDown();
	$(".main-menu").hide();
});

$(".video").parent("a").click(function(){
	$(this).parents(".post").addClass("popUpHide");
	$(".popUp-div").load("/"+contextPath+"/servlet/blogger/posts.viewVideo").slideDown();
	$(".main-menu").hide();
});
  
$(".search").click(function(){
	$(".input-search").toggle("fast");
});

$(".audio-Upload").click(function(){
	$(".upload-audio-box").toggle("fast");
});
  
  
  $(".URL").click(function(){
	$(".input-url").toggle("fast");
});
  
  
  $(".popup-blog-name > a").click(function(){
    $(".popup-menu").slideDown();
  });
  $(".post-menu-item > a").click(function(){
    $(".popup-menu").slideUp();
  });
  
  
  
  $(".arrow-pop").click(function(){
    $(".publish-menu").slideToggle();
  });
    
  $(".search > input").click(function(){
    $(".search-menu").slideDown();
  });
  
   $(".click-url").click(function(){
    $(".link-url").toggle();
  });
   
   $(".search-user").click(function(){
		   
	    $(".follower-menu").slideToggle();
	    
	  }); 
});



 
  
  

  
