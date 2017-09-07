/**
 * 
 */

/*------------------全局变量--------------------------*/
var portTablePara=[];
/*----------------读取表格中的项-----------------*/
$(document).ready(function(){
	var trs = document.querySelectorAll("#portTableDetail tbody tr");
	 var cablePara=[];
	 for (var i=0;i<trs.length;i++){
	    if($(trs[i]).hasClass("currtr")){
	    	portTablePara=[
	                      	trs[i].children[1].innerHTML,  //id
	                      	trs[i].children[2].innerHTML,   //name
	                      	trs[i].children[4].innerHTML,   //equipment type
	                      	trs[i].children[5].innerHTML,  //equipment id
	                      	trs[i].children[8].innerHTML   //description
	                     ];
	    }
	 }
  $("#portId").val(portTablePara[0]);
  $("#portName").val(portTablePara[1]);
  $("#description").val(portTablePara[4]);
	 
});

/*---------------------------------close按键----------------------------------------------*/
	 $(".sidebar_close").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 
	 
	 /*---------------------------------cancel按键----------------------------------------------*/
	 $(".cancel").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 
	 /*---------------------------------确定按键----------------------------------------------*/
	 $(".confirm").click(function(){
		 var type="framePort";
		 if(portTablePara[2]=="RTU")
		  {
			 type="rtuPort";
		  }
		 $.ajax({
			    url:'port/modifyPort',
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		        	   "type":type,
		        	   "id":$("#portId").val(),
		        	   "name":$("#portName").val(),
		        	   "description":$("#description").val()
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
 										refreashTable();
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
        
     });
	
	 
	 /*------------------刷新列表------------------*/
	 function refreashTable(){
		 var equiId=parseInt(portTablePara[3]);
		 if(portTablePara[2]=="配线架"){
			 getFramePort(equiId);
		 }
		 if(portTablePara[2]=="RTU"){
			 getRtuPort(equiId);
		 }
	 }
	  /*-------------frame---------------------*/
	   function getFramePort(frameId){
		   var trs = document.querySelectorAll("#portTableDetail tbody tr td");
	 	    for (var i=0;i<trs.length;i++){
	 		    trs[i].innerHTML = "";
		    }//先清空
	 	    $.ajax({
			    	 url:"getFramePortByFrameId",
			    	 dataType:'json', //接受数据格式
		             async: false,
		             data:{
		                     "id":frameId,           	 
		                  },
					 success: function(json){
						 if(json[0].status){
							  var portPara=[];
							  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
									 equiPara=[
	                                            json[jsonCount].id,
	                                            json[jsonCount].name,
	                                            json[jsonCount].order,
	                                            json[jsonCount].equiType,
	                                            json[jsonCount].equiId,
	                                            json[jsonCount].equiName,
	                                            json[jsonCount].status,
	                                            json[jsonCount].description,
	                                            json[jsonCount].alterUser,
	                                            json[jsonCount].alterTime
									          ];
									 portPara.push(equiPara);
						         }
							  showTable(portPara);
					        
						 }
				         
					 },
				 
		     });
		    
	   }
	   
	   /*-------------RTU---------------------*/
	   function getRtuPort(rtuId){
		   var trs = document.querySelectorAll("#portTableDetail tbody tr td");
	 	    for (var i=0;i<trs.length;i++){
	 		    trs[i].innerHTML = "";
		    }//先清空
	          $.ajax({
			    	 url:"getRtuPortByRtuId",
			    	 dataType:'json', //接受数据格式
		             async: false,
		             data:{
		                     "id":rtuId,           	 
		                  },
					 success: function(json){
						 if(json[0].status){
							  var portPara=[]; 
							  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
								var equiPara=[
											json[jsonCount].id,
											json[jsonCount].name,
											json[jsonCount].order,
											json[jsonCount].equiType,
											json[jsonCount].equiId,
											json[jsonCount].equiName,
											json[jsonCount].status,
											json[jsonCount].description,
											json[jsonCount].alterUser,
											json[jsonCount].alterTime
								          ];
								portPara.push(equiPara);
					         }
							  showTable(portPara);
						 }
				         
					 },
				 
		     });
		    
	   }
	  
	 