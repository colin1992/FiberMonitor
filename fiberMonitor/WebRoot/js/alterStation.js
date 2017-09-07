/**
 * 修改局站时读取原来的信息填充
 */
/*---------读取表格的值，给con赋值-------*/
$(document).ready(function(){
 var trs = document.querySelectorAll("#stationTableDetail tbody tr");
 var staPara=[];
 for (var i=0;i<trs.length;i++){
    if($(trs[i]).hasClass("currtr")){
       staPara =[
    		      trs[i].children[1].innerHTML,
    		      trs[i].children[2].innerHTML,
    		      trs[i].children[4].innerHTML,
    		      trs[i].children[5].innerHTML,
    		      trs[i].children[6].innerHTML,
    		      trs[i].children[7].innerHTML,
    		  ];
    }
 }
 /**坐标输入只能为数字**/
 $(".LongAndLatitude").focus(function(){
 	   $(this).keypress(function (event) {
            var eventObj = event || e;
            var keyCode = eventObj.keyCode || eventObj.which;
            if ((keyCode >= 48 && keyCode <= 57)||(keyCode==46))
                return true;
            else
                return false;
 	  	})
}) 
  /*----------------给station各项设置值--------------*/
	/*-------读取表格中的经纬度值，进行拆分------*/
	 var Longtitude=staPara[3];
	 var Latitude=staPara[4];
	 var LongtitudeOrientation=Longtitude.substr(0,2);
	 var lng=Longtitude.substr(2,Longtitude.length);
	 var LatitudeOrientation=Latitude.substr(0,2);
	 var lat=Latitude.substr(2,Longtitude.length);
	 $("#stationId").val(staPara[0]);
	 $("#stationName").val(staPara[1]);
	 $("#belongAreaName").val(staPara[2]);
	 $("#description").val(staPara[5]);//读取局站原来的参数
	 $("#LongtitudeOrientation").val(LongtitudeOrientation);
	 $("#lng").val(lng);
	 $("#LatitudeOrientation").val(LatitudeOrientation);
	 $("#lat").val(lat);

		 
});
/*---------------close-------------------------*/
$(".sidebar_close").click(function(){
    $(".contentDiv").html("");
    $(".sidebarDiv").html("");
});
/*-----------------cancel-----------------------*/
$(".cancel").click(function(){
	 $(".contentDiv").html("");
     $(".sidebarDiv").html("");
});
/**经纬度只能为数字**/
$(".lngAndLat").focus(function(){
	   $(this).keypress(function (event) {
         var eventObj = event || e;
         var keyCode = eventObj.keyCode || eventObj.which;
         if ((keyCode >= 48 && keyCode <= 57)||(keyCode==46))
             return true;
         else
             return false;
	  	})
 })
/*---------------confirm-------------------------*/
$(".confirm").click(function(){
	var lng=$("#lng").val();
	var lat=$("#lat").val();
	var validate=false;
	if(lng.indexOf(".")>0&&lat.indexOf(".")>0&&(!isNaN(lat))&&(!isNaN(lng))&&(parseInt(lat)>0&&parseInt(lat)<180)&&(parseInt(lng)>0&&parseInt(lng)<180)){//必须含有小数点,必须为数字
		   validate=true;
	}
	else{
		     validate=false;
		     var txt="经纬度格式不正确，经纬度必须为0-180范围内的小数。请重新输入经纬度"
		     var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
							
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
	}
	if(validate){
		$.ajax({
	       	 url:"station/modifyStation",//服务器地址
	       	 type:"POST",//采用POST请求
	       	 dataType:"json",//
	       	 timeout:2000,
	       	 data:{
	             	'stationId':$("#stationId").val(),
	             	'areaId': thisAreaId,
	                'stationName':$("#stationName").val(),
	                'description':$("#description").val(),
	                'stationLongitude':$("#LongtitudeOrientation").val()
					  				  +lng,
					 "stationLatitude":$("#LatitudeOrientation").val()
					                   +lat
					                   
	       	  },
	       	success: function(json){
	       	        $(".sidebarDiv").html("");
			    	var txt="";
			    	if(json[0].status){
			    		txt+="修改成功<br/>"
			    	}
			    	else{
			    		txt+="修改失败，请重试";
			    	}
				     var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
									getStationByAreaId(thisAreaId);
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
			      },
			    error:function(XMLHttpRequest,Error)
			      {
			    	    $(".sidebarDiv").html("");
			    	    var txt="修改失败<br/>";
			    	    txt+="失败原因：";
			    	    if(XMLHttpRequest.status==401){
			    	        txt+="您不具有当前操作的权限<br/>";
			    	     }
			    	     else{
			    	        	txt+="网络错误<br/>";
			    	        	txt+="状态码："+XMLHttpRequest.status;
			    	        }
			    	      var option={
			    	   					title: "提示",
			    	   					btn: parseInt("0001",2),
			    	   					onOk: function(){//点击确认的执行方法
			    	   						
			    	  			       }
			    	   				}
			    	   window.wxc.xcConfirm(txt, "info", option);
			  }
	   });
	}
    
});