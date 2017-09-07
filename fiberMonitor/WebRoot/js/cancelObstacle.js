/*
*
*
*/

/*----------------全局变量---------------*/
var rtuId=0;//当前rtuId  
var pick;//光路选择列表
var routeList=[];

/*页面载入时设置光路选择框，
*
*
*/
$(document).ready(function(){
	 
	rtuId=$(".obstacleTableDetail tbody tr")[0].children[4].innerHTML;
	getOnObstacleRoutes();//获取光路 
	/*------设置光路复选框-------*/
	pick = $("#pickList").pickList({data:routeList});
	
});

/*--------查询数据库，获得当前RTU下光路的id和name----------*/
function getOnObstacleRoutes(){
  $.ajax({
        url:"getOnObstacleRoutes",
        dataType:'json', //接受数据格式
        async: false,
        data:{
                "rtuId":rtuId,   
              },
	    success: function(json){
		    if(json[0].status){//查询成功
		         routeList=json[1];
		    }
		},
	   error: function(){
			    
		},
   })
 
}
 /*------------完成所有组的配置后下发--------*/
  $("#submit").click(function () {
	  var selected=pick.getValues();
	  if(selected.length>0){//选择了光路
		  var selectSNoAndId=[];
		  for(var index=0;index<selected.length;index++){
			  var thisSNo=selected[index].id.split(",")[0];
			  var thisId=selected[index].id.split(",")[1];
			  //alert("thisSNo:"+thisSNo);
			  selectSNoAndId.push([thisSNo,thisId]);
		  }
		 calcelObstacle(rtuId,selectSNoAndId);
	  }
	  else{
		  var txt= "您尚未选择任何光路，青先选择您要取消障碍告警测试的光路";  
			   var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
							
						}
					}
			  window.wxc.xcConfirm(txt, "info", option); 
	  }
	  
	 
  });
  /*-------------------取消障碍告警设置--------------*/
  function calcelObstacle(CM,selectSNoAndId){
  	
  	 $.ajax({
		    	 url:"curve/obstacleTest/cancelObstacle",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "CM":CM, 
	                     "SNoAndId":JSON.stringify(selectSNoAndId),
	                  },
				 success: function(json){
					 refreashTable(CM);//刷新表格;
					 if(json[0].status){
						   var txt= "您已成功取消当前光路的障碍告警测试";  
	      				   var option = {
	      							title: "提示",
	      							btn: parseInt("0001",2),
	      							onOk: function(){//点击确认的执行方法
	      								
	      							}
	      						}
	      				  window.wxc.xcConfirm(txt, "info", option); 
						   
					   }
					 else{
						 var txt= "取消失败，请重试"; 
						 txt+="错误原因："+json[0].err;    
	      				   var option = {
	      							title: "提示",
	      							btn: parseInt("0001",2),
	      							onOk: function(){//点击确认的执行方法
	      								
	      							}
	      						}
	      				  window.wxc.xcConfirm(txt, "info", option); 
					 }
			         
				 },
				 error:function(XMLHttpRequest,textStatus){
					 var txt= "取消失败<br/>";
					 txt+="失败原因：";
					 if(XMLHttpRequest.status==401){
			  			    txt+="您不具有当前操作的权限<br/>请联系管理员获得权限";
			  			}
			  		 else if(XMLHttpRequest.status==0){
			  				txt+="连接超时";
			  			}
			  		else{
	  			    	txt+="网络错误<br/>";
	  			    	txt+="状态码："+XMLHttpRequest.status;
	  			     }
				       var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
									
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
					},
			 
	     });
  	   $(".sidebarDiv").html(""); 
       $(".contentDiv").html("");
  }
  /*------------刷新显示表格---------------*/
 function refreashTable(CM){
	  var trs = document.querySelectorAll("#obstacleTableDetail tbody tr td");
	    for (var i=0;i<trs.length;i++){
		    trs[i].innerHTML = "";
		}//先清空
	    $.ajax({
		    	 url:"getObstacleRouteByRtuId",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "id":CM,           	 
	                  },
				 success: function(json){
					 if(json[0].status){
						 var tds = document.querySelectorAll("#obstacleTableDetail tbody tr td");
					 	    for (var i=0;i<tds.length;i++){
					 	        tds[i].innerHTML = "";
					 	    }
					           
							   for(var count=1;count<json.length;count++){
								   var equiPara=[
								              count,
								              json[count].id,
								              json[count].name,
								              json[count].stationAName,
								              json[count].rtuId,
								              json[count].rtuName,
								              json[count].rtuOrder,
								              json[count].stationZName,
								              json[count].frameId,
								              json[count].frameName,
								              json[count].frameOrder,
								              json[count].length,
								              json[count].status,
								              json[count].isObstacle,
								              json[count].priorityId,
								              json[count].priorityName,
								              json[count].description
								            ];
								   // alert(equiPara);
								   setTable(equiPara);   
							   }
					   }
			         
				 },
			 
	     });
  }
  
  function setTable(tableData) {      
 	 //  alert("table");
   	   var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
   	    for(var i=0; i<trs.length; i++){
   	        var Ele = trs[i].children;
   	        if(!Ele[0].innerHTML){
   	        	Ele[0]=i;
   	            for (var j=0;j<tableData.length; j++){
   	                Ele[j].innerHTML =tableData[j];
   	                i = trs.length;
   	            }
   	        }
   	    }
   	}
  
    
  
 /*---------取消------------*/
 $("#cancel").click(function(){
      $(".sidebarDiv").html("");
      $(".contentDiv").html("");
})