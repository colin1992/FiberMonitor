/**
 * 修改局站时读取原来的信息填充
 */
/*---------读取表格的值，给con赋值-------*/
$(document).ready(function(){
 var trs = document.querySelectorAll("#routeTableDetail tbody tr");
 var staPara=[];
 for (var i=0;i<trs.length;i++){
    if($(trs[i]).hasClass("currtr")){
       staPara =[
    		      trs[i].children[1].innerHTML,//光路标识
    		      trs[i].children[2].innerHTML,//光路名字
    		      trs[i].children[12].innerHTML,//光路描述
    		  ];
       getRouteMoudleType(staPara[0]);
    }
    
 }
 function getRouteMoudleType(routeId){
	 /**获取光路所在RTU端口的模块类型，从而限定当前光路的类型
	  *如果在在线模块：光路只能为在线
	    如果在备纤模块：只能为备纤
	    如果在混合模块：可以选择在线或备纤

	  ***/
	 $.ajax({
	 	 url:"getRouteMoudleType",
	 	 dataType:'json', //接受数据格式
	      async: false,
	      data:{"routeId":routeId},
	 	 success: function(json){
	 		 if(json[0].status){
	 			  var moudleType=json[0].moudleType;//模块类型
	 			  $("#alterRouteLink").val(String(json[0].isUplink));
	 			  $("#alterRouteStatus").val(String(json[0].isOnline));
	 			  switch(moudleType){
	 				case"1": 
	 				case "2"://在线 
	 				    $("#alterRouteStatus").attr("disabled","disabled");
	 					break;
	 				case"3"://保护-主
	 					if(json[0].isProtect){//处于保护状态的光路不能修改状态
	 					    $("#alterRouteStatus").attr("disabled","disabled");
	 					}
	 					$("#alterRouteLink").attr("disabled","disabled");
	 					break;
	 				default:
	 					break;
	 			}
	 		}
	 	},	 
	 })
 }

  /**----------------给光路各项设置值--------------*/

	 $("#alterRouteId").val(staPara[0]);
	 $("#alterRouteName").val(staPara[1]);
	 $("#alterRouteDescription").val(staPara[2]);//读取局站原来的参数
		 
});
/*---------------close-------------------------*/
$(".sidebar_close").click(function(){
    $(".contentDiv").html("");
    $(".sidebarDiv").html("");
});
/*-----------------cancel-----------------------*/
$("#cancel").click(function(){
	 $(".contentDiv").html("");
     $(".sidebarDiv").html("");
});
/*---------------confirm-------------------------*/
$(".confirm").click(function(){
	
    $.ajax({
	       	 url:"route/modifyRoute",//服务器地址
	       	 type:"POST",//采用POST请求
	       	 dataType:"json",//
	       	 timeout:2000,
	       	 data:{
	             	"routeId":$("#alterRouteId").val(),
	             	"routeName":$("#alterRouteName").val(),
	             	"routeStatus":$("#alterRouteStatus").val(),
	             	"linkStatus":$("#alterRouteLink").val(),
	             	"routeDescription":$("#alterRouteDescription").val(),
	       	  },
	       	 success: function(json){
		        	$(".sidebarDiv").html("");
			  		var txt="";
			  		if(json[0].status){
			  			  txt+="修改成功<br/>"
			  		}
			  		else{
			  			  txt+="修改失败，失败原因："+json[0].err;
			  		}
			  		var option = {
			  						title: "提示",
			  						btn: parseInt("0001",2),
			  						onOk: function(){//点击确认的执行方法
			  							getRouteByRtuId(json[0].rtuId);
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