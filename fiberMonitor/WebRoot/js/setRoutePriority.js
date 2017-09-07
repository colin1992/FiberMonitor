/**
*
*实现光路级别配置的复选
应该实现每次配置完的光路在下一个优先级配置时不再出现
*/

/*----------------全局变量---------------*/
var rtuId=0;
//当前rtuId  
var priorityId=[];
var priorityName=[];
var priorityDescription=[];
var pick;//光路选择列表
var routeList=[];//光路信息
var groupList=[];//分组
var selectJsonList=[];
var thisId=0;//当前选择组的id

//routeList为一个json数组，每个json对象包含两个元素，id和text  
//id为光路的id，text为光路的名称

/**页面载入时设置优先级选择框和光路选择框，
*初始时光路选项为空
*
*/

$(document).ready(function(){
	 /**------设置光路复选框-------*/
	rtuId=$(".obstacleTableDetail tbody tr")[0].children[4].innerHTML;
	pick = $("#pickList").pickList({data:routeList});
  /**------设置优先级别下拉列表--------*/
   var status=getPrioritys();
   if(status){   
	  var option='';
      for(var count=0;count<priorityId.length;count++){
    	  option+="<li><a href='#' id="+priorityId[count]+">"+priorityName[count]+"</a></li>";
       }
      $("#LevelList").append(option);
     // alert(option);
   } 
  getUnSetLevelRoutes();//获取光路 
});

/**------------------优先级别下拉框状态变化------------------*/
$("#dropdown ul li a").live('click',function () {
	     var txt=$(this).text();//text
	     $(".priorityLevel").val(txt);
	     thisId= $(this).attr('id');//value
	     $("#dropdown ul").hide();
	     getUnSetLevelRoutes();//获取光路 
		 deleteGroupedRoute();//移除已配置
		 var thisSelect=[];
	       for(var index=0;index<selectJsonList.length;index++){
	    	   if(parseInt(selectJsonList[index][0])==parseInt(thisId)){
	    		   thisSelect=selectJsonList[index][1];//读取已经选择光路的Json格式数据
	    		   break;
	    	   }
	       }
       /**---------刷新光路列表----------*/ 
       pick.reSet(routeList);
       pick.setSelectedList(thisSelect);
});
    
    /**---------------鼠标移到某项，主要是为了用于删除---------------*/
 $("#dropdown ul li a").live('mouseenter',function () {
        thisId=$(this).attr("id");
   });


/**------------点击弹出下拉框------*/
$(".priorityLevel").click(function(){
	  addMenu();//添加菜单
	  var ul = $("#dropdown ul");
	  if(ul.css("display")=="none"){
	  ul.slideDown("fast");
	  }
	  else{
	      ul.slideUp("fast");
	  }
});

/**-----------------为光路组添加右键菜单---------------------*/
 function addMenu(){
	var menu = new BootstrapMenu('#dropdown ul li a', {
	     actions: [{
	         name: '修改告警组',
	         onClick: function() {
	        	 $(this).remove();
	        	 localStorage.setItem("thisGroupId",thisId);//在localStorage中存入当前告警组Id，为了便于修改页面使用
	        	 $("#alarmGroupArea").css("display","block");
	        	 $("#alarmGroupArea").load("html/curve/alarmGroupAlter.html");
	         }
	     },
	     {
	         name: '删除告警组',
	         onClick: function() {
	        	 $(this).remove();
	        	  var txt= "删除告警组会取消当前组下光路的障碍告警测试，请谨慎选择，";
	        	  txt+="确认继续请点击\"确认键\"";
				  var option = {
							title: "提示",
							btn: parseInt("0011",2),
							onOk: function(){//点击确认的执行方法
							  deleteAlarmGroup(thisId);
						 }
						}
				  window.wxc.xcConfirm(txt, "info", option);
	         }
	       },
	         {
	    	   name: '增加告警组',
		         onClick: function() {
		        	 $(this).remove();
		        	 $("#alarmGroupArea").css("display","block");
		        	 $("#alarmGroupArea").load("html/curve/addAlarmGroup.html");
		         }
	     }]
	 });
 }  
 
/**-----------删除告警组-------------*/
 function deleteAlarmGroup(groupId){
	 $.ajax({
	        url:"deleteAlarmGroup",
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	                "id":groupId,  
	                "CM":rtuId
	              },
		    success: function(json){
			   if(json[0].status){//delete成功
				   getRouteByRtuId(rtuId);//刷新表格
				   var txt= "您已成功删除当前告警组\n";
				   var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								var status=getPrioritys();
								   if(status){   
									  var option='';
									  $("#LevelList").empty();//先清空
								      for(var count=0;count<priorityId.length;count++){
								    	  option+="<li><a href='#' id="+priorityId[count]+">"+priorityName[count]+"</a></li>";
								       }
								      $("#LevelList").append(option);
								   } 
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
				}
			   else{
				   var txt= "删除失败\n";
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
		  error:function(XMLHttpRequest,textStatus,F){
			  var txt= "删除失败，网络错误\n";
			  txt+="状态码："+XMLHttpRequest.status+"\n";
			  txt+="错误原因：" +XMLHttpRequest.statusText;    
		       var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
							
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
			},
	   })
 }
		 
 
/**--------查询数据库，获得当前RTU下所有告警组的id和name----------*/
function getPrioritys(){
   var checkStatus=false;
   $.ajax({
        url:"getPriorityLevel",
        dataType:'json', //接受数据格式
        async: false,
        data:{
                "rtuId":rtuId,   
              },
	    success: function(json){
	    	if(json[0].status){//查询成功
			   priorityId=json[0].id;
               priorityName=json[0].name;
               priorityDescription=json[0].description;
               checkStatus=true;
		    }
		},
	   error: function(){
		   
		},
   })
  return checkStatus;
}
/*--------查询数据库，获得当前RTU下未配置优先级的光路的id和name----------*/
function getUnSetLevelRoutes(){
   var checkStatus=false;
   $.ajax({
        url:"getRoutes",
        dataType:'json', //接受数据格式
        async: false,
        data:{
                "rtuId":rtuId,   
              },
	    success: function(json){
		    if(json[0].status){//查询成功
		         routeList=json[1];
		         checkStatus=true;
            }
		},
	   error: function(){
			    
		},
   })
  return checkStatus;
}
/**--------------清除routeList中已经分组的光路---------------*/
function deleteGroupedRoute(){
	 $.each(routeList, function (key, val) {
		 for(var index=0;index<groupList.length;index++){
			 for(var count=0;count<groupList[index].length;count++){
				 if(parseInt(val.id)==parseInt(groupList[index][1][count]))
					 {
					    delete routeList[key]; //移除已经分组的光路
					    break;
					 }
			 }
		 }
		
      });
}
/**------------完成一组设置后暂存--------*/
  $("#getSelected").click(function () {
      var oneGroup=[];
      var isExit=false;
      var selected=pick.getValues();
       for(var count=0;count<selected.length;count++){
    	   oneGroup.push(selected[count].id)
       }
       for(var index=0;index<groupList.length;index++){
    	  if(groupList[index][0]==thisId){//已经存在则替换
    		   groupList[index]=groupList.push([thisId,oneGroup]);
    		   isExit=true;
    		   break;
    	   }
       }
       if(!isExit){  //不存在则添加
    	   groupList.push([thisId,oneGroup]);
       }
       isExit=false;
       for(var index=0;index<selectJsonList.length;index++){
    	   if(selectJsonList[index][0]==thisId){
    		   selectJsonList[index]= selectJsonList.push([thisId,selected]);
    		   isExit=true;
    		   break;
    	   }
       }
       if(!isExit){  //不存在则添加
          selectJsonList.push([thisId,selected]);//将选择光路的json格式数据暂存
       }
       getUnSetLevelRoutes();
       deleteGroupedRoute();//从routeList中删除已经分组的光路
       pick.reSet(routeList);//刷新显示
       pick.setSelectedList(selected);
       var txt= "您已成功设置当前告警组，点击确认设置下一组";
       var option = {
				title: "提示",
				btn: parseInt("0001",2),
				onOk: function(){//点击确认的执行方法
					setNextGroup(thisId);
				}
			}
	  window.wxc.xcConfirm(txt, "info", option);
       
  });
  /**------------设置当前组的下一组----------*/
  function setNextGroup(thisGroupId){
	  var nextGroupId=0;
	  for(var index=0;index<priorityId.length;index++){
		  if(thisGroupId==priorityId[index]){
			  if(index<priorityId.length-1){
				  nextGroupId=priorityId[index+1];
			  }
			  break;
		  }
	  }
	  if(nextGroupId==0){//全部完成
		  var txt= "您已配置完成所有告警组，点击\"完成\"下发配置";
	       var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
						
					}
				}
		  window.wxc.xcConfirm(txt, "info", option);
	  }
	  $("#dropdown ul li a[id="+nextGroupId+"]").trigger("click");
	  getUnSetLevelRoutes();//获取光路 
	  deleteGroupedRoute();//移除已配置
      var thisSelect=[];
      for(var index=0;index<selectJsonList.length;index++){
   	   if(parseInt(selectJsonList[index][0])==parseInt(thisId)){
   		   thisSelect=selectJsonList[index][1];//读取已经选择光路的Json格式数据
   		   break;
   	   }
      }
     
       /**---------刷新光路列表----------*/ 
       pick.reSet(routeList);
       pick.setSelectedList(thisSelect);
  }
  /**------------完成所有组的配置后下发--------*/
  $("#submit").click(function () {
	  var groupArray=[];
	  for(var index=0;index<groupList.length;index++){
		  var groupPara=[];
		  var routeIdsString="";
		  for(var count=1;count<groupList[index].length;count++){
			  routeIdsString+=groupList[index][count];
			  if(count<groupList[index].length-1){
				  routeIdsString+=",";
			  }
		  }
		  groupPara=[groupList[index][0],routeIdsString];
		  groupArray.push(groupPara);
	  }
	  $.ajax({
		  url:"addGroupInfo",
          type:'post', //数据发送方式
          dataType:'json', //接受数据格式
          async: false,
          data:{
        	      "rtuId":rtuId, 
        	      "groupArray": JSON.stringify(groupArray),
                },
		  success: function(json){
			  
			  if(json[0].status){
				  var txt= "告警组配置成功！\n";
				  var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								 getRouteByRtuId(rtuId);//刷新表格
						 }
						}
				  window.wxc.xcConfirm(txt, "info", option);
			  }
			  else{
				   var txt= "配置失败\n";
				   txt+="错误原因：服务器端数据库存储故障";    
			       var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								 getRouteByRtuId(rtuId);//刷新表格
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
			  }
		  },
		  error:function(XMLHttpRequest,textStatus,F){
			  var txt= "配置失败，网络错误。";
			  txt+="状态码："+XMLHttpRequest.status+"。";
			  txt+="错误原因：" +XMLHttpRequest.statusText;    
		       var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
							
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
			},
	  })
	  $(".sidebarDiv").html(""); 
      $(".contentDiv").html("");
  });
  
 /**---------取消------------*/
 $("#cancel").click(function(){
      $(".sidebarDiv").html("");
      $(".contentDiv").html("");
})