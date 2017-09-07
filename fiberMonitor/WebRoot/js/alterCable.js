/**
 * 修改cable
 */
/**
 * 在载入增加cable对话框时执行
 * 查询数据库，查找所有区域
 */

/*----------------页面载入后读取表格数据------------------------------*/
$(document).ready(function(){
	var trs = document.querySelectorAll("#cableTableDetail tbody tr");
	 var cablePara=[];
	 for (var i=0;i<trs.length;i++){
	    if($(trs[i]).hasClass("currtr")){
	    	for(var count=1;count<14;count++)//读取欲取的值
	       cablePara.push(trs[i].children[count].innerHTML);
	    		     
	    }
	 }
	 $("#cableId").val(cablePara[0]);//id
	 $("#cableName").val(cablePara[1]);//name
	 $("#cableLength").val(cablePara[2]);//length
	 $("#AStationName").val(cablePara[3]);// A Station
	 $("#ARemainLen").val(cablePara[4]);// A remain
	 $("#ZStationName").val(cablePara[5]);// Z Station
	 $("#ZRemainLen").val(cablePara[6]);// A remain
	 $("#newworkLevel").val(cablePara[7]);// newworkLevel
	 $("#laidWay").val(cablePara[8]);// laidWay
	 $("#cableCore").val(cablePara[9]);//cableCore
	 $("#cableCoreCount").val(cablePara[10]);//cableCoreCount
	 $("#refractiveIndex").val(cablePara[11]);//refractiveIndex
	 $("#description").val(cablePara[12]);//description
	
	
});

	/*---------------------------------close按键----------------------------------------------*/
	 $(".sidebar_close").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 
	 /**--------------------------------确定按键----------------------------------------------*/
	 $(".confirm").click(function(){
		 var stationId=$("#AStationName").val();
	 	$.ajax({
			    url:'cable/modifyCable',
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		        	   "cableId":$("#cableId").val(),
		        	   "cableName":$("#cableName").val(),
		        	   "cableLength":$("#cableLength").val(),
		        	   "ARemainLen":$("#ARemainLen").val(),
		        	   "ZRemainLen":$("#ZRemainLen").val(),
		        	   "networkLevel":$("#newworkLevel").val(),
		        	   "laidWay":$("#laidWay").val(),
		        	   "cableCoreCount":$("#cableCoreCount").val(),
		        	   "cableCore":$("#cableCore").val(),
		        	   "refractiveIndex":$("#refractiveIndex").val(),
		        	   "description":$("#description").val(),
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
			  							getCableByStationId(currentStationId);//通过局站id获取与该局站相关的所有光缆

			  						}
			  					}
			  		window.wxc.xcConfirm(txt, "info", option);
			  			      },
			  	error:function(XMLHttpRequest,Error){
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
        
     });
	 /*-------------------------------------------cancel---------------------------*/
	 $(".cancel").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 
	
	 
	 
	    
