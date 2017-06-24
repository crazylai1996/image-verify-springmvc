// JavaScript Document
$(function(){
	var maxWidth=400;
	var maxHeight=200;
	var offset=30;
	var verifyCode="";
	$(".img-wrapper").click(function(e){
		var offsetx=e.offsetX;
		var offsety=e.offsetY;
		if(offsety<30){
			return ;
		}
		var leftVal=offsetx-12;
		var topVal=offsety-12;
		
		leftVal=leftVal<0?0:leftVal;
		leftVal=leftVal>maxWidth-24?maxWidth-24:leftVal;
		topVal=topVal<offset?offset:topVal;
		topVal=topVal>maxHeight+offset-24?maxHeight+offset-24:topVal;
		
		var pickDiv=$("<div class='pick-div'><img src='assets/pickon.png'/></div>");
		pickDiv.css("position","absolute");
		pickDiv.css("left",leftVal+"px");
		pickDiv.css("top",topVal+"px");
		
		verifyCode+=leftVal+","+(topVal-offset)+";";
		//console.log(verifyCode);
		$(this).append(pickDiv);
	});
	$(".img-wrapper").on("click",".pick-div",function(e){
		//console.log(verifyCode);
		var target=$(this)[0].offsetLeft+","+$(this)[0].offsetTop+";";
		verifyCode=verifyCode.replace(target,"");
		
		$(this).remove();
		e.stopPropagation();
	});
	$(".refresh-btn").click(function(){
		verifyCode="";
		$(".pick-div").remove();
		$(".img-wrapper img").attr("src","getVerifyCode.html?date="+new Date().getTime());
	});
	$(".submit-btn").click(function(){
		verifyCode=verifyCode.substring(0,verifyCode.length-1);
//		console.log(verifyCode);
		$.ajax({
            url:"verify.html",
            data:{"verifyCode":verifyCode},
            type:"POST",
            //dataType:"json",
            success:function(data){
                alert(data);
                $(".refresh-btn").click();
            }
        });
	});
});