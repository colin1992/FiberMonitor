/**
 * 
 */

$(document).ready(function(){
	var trs = document.querySelectorAll("#portTableDetail tbody tr");
	 var portTablePara=[];
	 for (var i=0;i<trs.length;i++){
	    if($(trs[i]).hasClass("currtr")){
	    	portTablePara=[
	                      	trs[i].children[1].innerHTML,  //id
	                      	trs[i].children[4].innerHTML,   //equipment type
	                      ];
	    }
	 }
	var spanArr=[];
	var id=parseInt(portTablePara[0]);
	if(portTablePara[1]=="配线架")
	 {
		spanArr=["连接设备类型","连接设备标识","连接设备名称"];   
	}
	else{
		spanArr=["跳纤类型","跳纤标识","跳纤名称"]; 
		
	}
	$("#spanConnectType").text(spanArr[0]);
	$("#spanConnectId").text(spanArr[1]);
	$("#spanConnectName").text(spanArr[2]);
	getPort(id);
});
/*-------------get port by id---------------------*/
function getPort(id){
	 $.ajax({
		    	 url:"port/checkPort",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "id":id,           	 
	                  },
				 success: function(json){
					 if(json[0].status){
						  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
							  equiPara=[
										json[jsonCount].id,
										json[jsonCount].name,
										json[jsonCount].order,
										json[jsonCount].status,
										json[jsonCount].description,
										json[jsonCount].equiType,
										json[jsonCount].equiId,
										json[jsonCount].equiName,
										json[jsonCount].connectType,
										json[jsonCount].connectId,
										json[jsonCount].connectName,
										json[jsonCount].alterTime,
										json[jsonCount].alterUser
							          ];
							 setCon(equiPara);
					         }
				     }
			     },
				 error:function(XMLHttpRequest,Error)
			      {
			    	    $(".sidebarDiv").html("");
			    	    var txt="操作失败<br/>";
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

/*-------------------------显示端口详细信息-----------------------*/
function setCon(equipara){
	$("#portId").val(equipara[0]);
	$("#portName").val(equipara[1]);
	$("#portOrder").val(equipara[2]);
	$("#portStatus").val(equipara[3]);
	$("#description").val(equipara[4]);
	$("#equiType").val(equipara[5]);
	$("#equiId").val(equipara[6]);
	$("#equiName").val(equipara[7]);
	$("#connectType").val(equipara[8]);
	$("#connectId").val(equipara[9]);
	$("#connectName").val(equipara[10]);
	$("#alterTime").val(equipara[11]);
	$("#alterUser").val(equipara[12]);
}
/*---------------------------------close----------------------------------------------*/
$(".sidebar_close").click(function(){
    $(".contentDiv").html("");
    $(".sidebarDiv").html("");
});


/*---------------------------------confirm按键----------------------------------------------*/
$(".confirm").click(function(){
    $(".contentDiv").html("");
    $(".sidebarDiv").html("");
});
