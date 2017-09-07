/**
 * 增加landmark
 */

/*-------------页面载入完成时候加载，将cableName设置为当前cable-------------*/  
    $(document).ready(function(){
	   $("#thisCableName").val(landmarkCableName);//landmarkCableName 变量定义在landmark.js中，为全局变量
    });
    $(function() {  
    	
    	$('input[name="integer"]').bind({  
            keydown : function() {  
                $(this).val($(this).val().replace(/[^\d]/g, ''));  
            }  
        });  
      
    	$('input[name="integer"]').each(function() {  
            var _input = $(this)[0];  
            if (_input.attachEvent) {  
                _input.attachEvent('onbeforepaste', formatPasteDataToInteger);  
            } else {  
                _input.addEventListener('onbeforepaste', formatPasteDataToInteger, false);  
            }  
        });  
          
    	$('input[name="float"]').bind({  
    		keydown : function() {  
                $(this).val($(this).val().replace(/[^0-9.]/g, ''));  
            }  
        });  
          
    	$('input[name="float"]').each(function() {  
            var _input = $(this)[0];  
            if (_input.attachEvent) {  
                _input.attachEvent('onbeforepaste', formatPasteDataToFloat);  
            } else {  
                _input.addEventListener('onbeforepaste', formatPasteDataToFloat, false);  
            }  
        });  
      
        function formatPasteDataToInteger() {  
            clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g, ''));  
        }  
      
        function formatPasteDataToFloat() {  
            clipboardData.setData('text', clipboardData.getData('text').replace(/[^0-9.]/g, ''));  
        }  
      
    });  
    
   /*----------------close------------------*/
    $(".sidebar_close").click(function(){
        $(".contentDiv").html("");
        $(".sidebarDiv").html("");
    });
    /**所有经纬度都只能是数字新形式**/
    $(".formFolat").focus(function(){
 	   $(this).keypress(function (event) {
            var eventObj = event || e;
            var keyCode = eventObj.keyCode || eventObj.which;
            if ((keyCode >= 48 && keyCode <= 57)||(keyCode==46))
                return true;
            else
                return false;
 	  	})
    })
   /*-------------------确认增加----------------*/
    $(".addConfirm").click(function(){
    	var validate=false;
    	var lng=$("#lngFloat").val();
    	var lat=$("#latFloat").val();
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
       		 url:'landmark/addLandmark',
                type:'post', //数据发送方式
                dataType:'json', //接受数据格式
                async: false,
                data:{
               	    "landmarkName":$("#addLandmarkName").val(),
               	    "cableId":landmarkCableId,//全局变量定义在 landmark.js 中
               	    "Longtitude":$("#LongtitudeOrientation").val()+lng,
               	    "Latitude":$("#LatitudeOrientation").val()+lat,
               	    "description":$("#landmarkDescription").val(),
               	  },
                success:function(json){
               	$(".sidebarDiv").html("");
               	        var txt="";
        			    	if(json[0].status){
        			    		txt+="增加成功<br/>"
        			    	}
        			    	else{
        			    		txt+="增加失败，请重试";
        			    	}
        				     var option = {
        								title: "提示",
        								btn: parseInt("0001",2),
        								onOk: function(){//点击确认的执行方法
        									
        								}
        							}
        					  window.wxc.xcConfirm(txt, "info", option);
        				    refreashTable(landmarkCableId);
        			      },
        			    error:function(XMLHttpRequest,Error)
        			      {
        			    	    $(".sidebarDiv").html("");
        			    	    var txt="增加失败,";
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
    /*-----------------取消增加-----------------*/
    $(".addCancel").click(function(){
        $(".contentDiv").html("");
        $(".sidebarDiv").html("");
    });
