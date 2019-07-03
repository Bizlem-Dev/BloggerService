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
    function showVideo(obj){
        $(obj).hide();
        $(obj).next().hide();
        $(obj).next('div').next('div').html($(obj).parent().parent().prev().val());
    }
    function closePop(){
        $("#bg-mask").hide();
        $(".main-menu").show();
        $(".popup-container").remove();
        $(".post").removeClass("popUpHide");
    }
    
    function closeEditBlock(obj){
    	//window.location.reload();
        $(obj).parent().parent().parent().hide();
        $(obj).parent().parent().parent().prev(".post-wrapper").show();
        $(obj).parent().parent().parent().html("");
        $("#bg-mask").hide();
        $(".post").removeClass("popUpHide");
    }
    function changeName(value,obj,result){
        $(obj).parent().children(".submitClass").removeClass("selected");
        $(obj).addClass("selected");
        $(obj).parent().parent().next("#submitButtonId").val(value);
        $(obj).parent().parent().next().next("#typeId").val(result);
        if(result=='Queued'){
          $(obj).children().children("#queuedTimeId").show();
        }else{
          $(obj).children().children("#queuedTimeId").hide();
          $(obj).parent().parent().hide();
        }
        
    }
    
    function showDiv(obj){
        $(obj).prev().prev().prev("#publish-menu").toggle();
    }
    
    function openBlogId(obj){
        $(obj).next("#blog-name-menu").slideDown();
    }
    
    function changeBlog(value,obj){
        $(obj).parent().parent().parent().children(".post-menu-item").removeClass("selected");
        $(obj).parent().parent().addClass("selected");
        $(obj).parent().parent().parent().parent().prev(".blog-name-change").text(value);
        $(obj).parent().parent().parent().parent().slideUp();
        $(obj).parent().parent().parent().parent().parent().parent().next("#blogDId").val(value);
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