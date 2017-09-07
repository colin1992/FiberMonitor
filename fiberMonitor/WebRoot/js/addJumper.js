/***
 * 
 */

/**----------------全局变量-------------------------*/
var frameJumperLabelArr=["A端配线架","A端端口号","Z端配线架","Z端端口号"];
var routeJumperLabelArr=["RTU名称","RTU端口号","配线架名称","配线架端口号"];
var frameId=[];
var frameName=[];
var rtuId=[];
var rtuName=[];
var stationName=localStorage.getItem("jumperStationName");
var stationId=localStorage.getItem("jumperStationId");
var aPortStr=[];
var aPortOrder=[];
var zPortStr=[];
var zPortOrder=[];
//全局变量，RTU模块名称
var modelName=[]; 
//全局变量，RTU模块序号
var modelOrder=[];
/**-------------------初始化隐藏RTU的模块选择----------------------*/
$("#aModelLabel").hide();
$("#aRtuModel").hide();

/**------------设置station值------------------*/
$(document).ready(function(){
    $("#stationName").val(stationName);
});
/**----------------设置各标签名-------------*/
function setLabel(labelArr){
	$("#aNameLabel").text(labelArr[0]);
	$("#aPortLabel").text(labelArr[1]);
	$("#zNameLabel").text(labelArr[2]);
	$("#zPortLabel").text(labelArr[3]);
}

/**------------------------跳纤类型下拉框状态改变-----------------------------------*/
$('#addJumperType').change(function(){
	var currentLabel=[];
	$("#addJumperType option[value='index']").remove(); 
	var jumperType=$(this).children('option:selected').val(); 
	/**--------将端口号下拉框复位-------*/
	$("#aEquipmentPort").empty();
	$("#zEquipmentPort").empty();
	$("#aEquipmentPort").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
	$("#zEquipmentPort").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
	 if(jumperType=="配线架跳纤"){
		 currentLabel=frameJumperLabelArr;
		 /**--------先将模块选择隐藏--------*/
		 $("#aModelLabel").hide();
		 $("#aRtuModel").hide();
		 /**--获取当前局站的配线架----*/
		 getFrame();
		 /**----------先将下拉框复位---------*/
		 $("#aEquipmentName").empty();
		 $("#zEquipmentName").empty();
		 $("#aEquipmentName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
		 $("#zEquipmentName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
		 /**--------设置配线架a------*/
		 for(var Index=0;Index<frameName.length;Index++){
			  $("#aEquipmentName").append("<option value='"+frameId[Index]+"'>"+frameName[Index]+"</option>");//给区域选项菜单赋值
	      }
		 /**--------设置配线架z------*/
		 for(var Index=0;Index<frameName.length;Index++){
			  $("#zEquipmentName").append("<option value='"+frameId[Index]+"'>"+frameName[Index]+"</option>");//给区域选项菜单赋值
	      }
		
	 }
	 else{
		
		 currentLabel=routeJumperLabelArr;
		 /***--------如果是光路跳纤，先将模块选择复原--------*/
		 if(jumperType=="光路跳纤"||jumperType=="切换跳纤")
			 {
			 	$("#aModelLabel").show();
			 	$("#aRtuModel").show();
			 }
		 else{//备纤光源跳纤
			 	$("#aModelLabel").hide();
			 	$("#aRtuModel").hide();
		 }
	 	 /**-----获取当前局站的RTU和配线架---------*/
		 getRtu();
		 getFrame();
		 /**----------先将下拉框复位---------*/
		 $("#aEquipmentName").empty();
		 $("#zEquipmentName").empty();
		 $("#aEquipmentName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
		 $("#zEquipmentName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
		 /**--------设置RTU-------*/
		 for(var Index=0;Index<rtuName.length;Index++){
			    $("#aEquipmentName").append("<option value='"+rtuId[Index]+"'>"+rtuName[Index]+"</option>");//给区域选项菜单赋值
	      }
		 /**--------设置配线架------*/
		 for(var Index=0;Index<frameName.length;Index++){
			    $("#zEquipmentName").append("<option value='"+frameId[Index]+"'>"+frameName[Index]+"</option>");//给区域选项菜单赋值
	      }
		}
	
	/**----------------设置label--------------------*/
	setLabel(currentLabel);
	   
});

/**----------------设备a状态改变-----------------------------*/
$('#aEquipmentName').change(function(){
	$("#aEquipmentName option[value='index']").remove(); 
	var aEquipmentid=$(this).children('option:selected').val(); 
    var aEquiId=parseInt(aEquipmentid);
    if($("#aNameLabel").text()=="A端配线架"){//为配线架    
    	  getFramePort(aEquiId,"a");
     }
    else if($("#addJumperType").val()=="光路跳纤"||$("#addJumperType").val()=="切换跳纤"){//切换跳纤或光路跳纤，获取RTU的模块
    	getRtuModelName(aEquiId);
    }
    else  //备纤光源RTU
    {
    	getRtuPort(aEquiId,0);
    }	
    /***----------设置a端口号-----------------*/
    /**----------先将下拉框复位---------*/
	 $("#aEquipmentPort").empty();
	 $("#aEquipmentPort").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
	 /**--------设置配设备a端口------*/
	 if($("#aNameLabel").text()=="A端配线架"){//为配线架
	    for(var Index=0;Index<aPortStr.length;Index++){
			  $("#aEquipmentPort").append("<option value='"+aPortOrder[Index]+"'>"+aPortStr[Index]+"</option>");//
	     }
	 }
	else if($("#addJumperType").val()=="光路跳纤"||$("#addJumperType").val()=="切换跳纤"){ //为光路跳纤或切换跳纤
		 $("#aRtuModel").empty();
		 $("#aRtuModel").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
		 for(var Index=0;Index<modelName.length;Index++){
			  $("#aRtuModel").append("<option value='"+modelOrder[Index]+"'>"+modelName[Index]+"</option>");//
	     }
	}
	 else{ //为切换跳纤或备纤光源跳纤
	     for(var Index=0;Index<aPortStr.length;Index++){
			  $("#aEquipmentPort").append("<option value='"+aPortOrder[Index]+"'>"+aPortStr[Index]+"</option>");//给端口选项菜单赋值
	     }
	}
	
});


/***----------------RTU模块状态改变----------------*/
$('#aRtuModel').change(function(){
	$("#aRtuModel option[value='index']").remove(); 
	var modelNameSelect=$(this).children('option:selected').val(); 
	var aEquipmentid=$("#aEquipmentName").children('option:selected').val(); 
    var aEquiId=parseInt(aEquipmentid);
    getRtuPort(aEquiId,modelNameSelect);
   /***----------设置a端口号-----------------*/
    /***----------先将下拉框复位---------*/
	 $("#aEquipmentPort").empty();
	 $("#aEquipmentPort").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
	 /***--------设置配设备a端口------*/
      for(var Index=0;Index<aPortOrder.length;Index++){
			  $("#aEquipmentPort").append("<option value='"+aPortOrder[Index]+"'>"+aPortStr[Index]+"</option>");//
	  }  
});
/***
 * 点击模块下拉框
 * **/
$('#aRtuModel').click(function(){
	if(modelName.length<1){
		  var txt="当前RTU下无用于连接该类跳纤的模块。";
	      var option={
	   					title: "提示",
	   					btn: parseInt("0001",2),
	   					onOk: function(){//点击确认的执行方法
	   						
	  			       }
	   				}
	   	window.wxc.xcConfirm(txt, "info", option); 
	}
})
/**----------------设备z状态改变-----------------------------*/
$('#zEquipmentName').change(function(){
	$("#zEquipmentName option[value='index']").remove(); 
	var zEquipmentid=$(this).children('option:selected').val(); 
	var zEquiId=parseInt(zEquipmentid);//z端设备只能是配线架
    getFramePort(zEquiId,"z");
	
	  /***----------设置z端口号-----------------*/
    /**----------先将下拉框复位---------*/
	 $("#zEquipmentPort").empty();
	 $("#zEquipmentPort").append("<option value='"+"index"+"'>"+"请选择"+"</option>");
	 /**--------设置配设备a端口------*/
	 for(var Index=0;Index<zPortStr.length;Index++){
		  $("#zEquipmentPort").append("<option value='"+zPortOrder[Index]+"'>"+zPortStr[Index]+"</option>");//给区域选项菜单赋值
     }
});

/***--------------a端口选择改变--------------*/
$('#aEquipmentPort').change(function(){
      $("#aEquipmentPort option[value='index']").remove(); 
})
/***--------------z端口选择改变--------------*/
$('#zEquipmentPort').change(function(){
      $("#zEquipmentPort option[value='index']").remove(); 
})
/***---------------------------获取配线架-------------------------------*/
function getFrame(){
	frameId=[];
	frameName=[];
	$.ajax({
			url:'getFrameByStationId',
		    type:'post', //数据发送方式
		    dataType:'json', //接受数据格式
		    async: false,
		    data:{
		    	   "stationId":stationId,
		    	 },
		     success: function(json){
		    	if(!json[0].status){
		    	   var txt="当前局站不存在配线架"; 
		    	   var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
							}
						}
				    window.wxc.xcConfirm(txt, "info", option);
		    	 }
		    	else{
		    		  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
		    			 frameId.push(json[jsonCount].id);
		    			 frameName.push(json[jsonCount].name);
		    		  }
		    	}
		     },
		     error:function(XMLHttpRequest,Error)
		      {
		    	   
		    	    var txt="获取配线架失败<br/>";
		    	    txt+="失败原因：";
		    	    txt+="网络错误<br/>";
		    	    txt+="状态码："+XMLHttpRequest.status;
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
/***---------------------------获取RTU-------------------------------*/
 function getRtu(){
	 rtuId=[];
	 rtuName=[];
	 var jumperType=$("#addJumperType").val();
	 $.ajax({
	   	    url:'getRtuByStationId',
	        type:'post', //数据发送方式
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	        	   "jumperType":jumperType,
	        	   "stationId":stationId,
	        	 },
	         success: function(json){
	        	if(!json[0].status){
	        		   var txt="当前局站不存在RTU。"; 
	        		   if(jumperType=="切换跳纤"){
	        			   txt="当前局站不存在切换RTU。";
	        		   }
	        		   else if(jumperType=="备纤光源跳纤"){
	        			   txt="当前局站不存在备纤光源RTU。";
	        		   }
			    	   var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
								}
							}
					    window.wxc.xcConfirm(txt, "info", option);
	        	 }
	        	else{
	        		  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
	        			  rtuId.push(json[jsonCount].id);
	        			  rtuName.push(json[jsonCount].name);
	        		  }
	        	}
	         },
	         error:function(XMLHttpRequest,Error){
	        	 var txt="获取RTU失败<br/>";
		    	    txt+="失败原因：";
		    	    txt+="网络错误<br/>";
		    	    txt+="状态码："+XMLHttpRequest.status;
		    	    var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
		    	   	window.wxc.xcConfirm(txt, "info", option);
	        },  
	   });
 }
 
/***------------------------获取RTU模块名字----------------------------*/
 function getRtuModelName(rtuId){
	 $.ajax({
	   	    url:'getRtuModelNameByRtuId',
	        type:'post', //数据发送方式
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	        	    "addJumperType":$("#addJumperType").val(),
	        	    "rtuId":rtuId,
	        	 },
	         success: function(data){
	        	      modelName=[];
	        	      modelOrder=[];
	        		  for(var i=0;i<data[0].modelName.length;i++){
	        			  modelName.push(data[0].modelName[i]);
	        			  modelOrder.push(data[0].modelOrder[i]);
	        		  }
	        		  if(modelName.length<1){
	        			    var txt="当前RTU下无用于连接该类跳纤的模块。";
				    	    var option={
				    	   					title: "提示",
				    	   					btn: parseInt("0001",2),
				    	   					onOk: function(){//点击确认的执行方法
				    	   						
				    	  			       }
				    	   				}
				    	   	window.wxc.xcConfirm(txt, "info", option); 
	        		  }
	         },
	         error:function(XMLHttpRequest,Error){
	        	    var txt="获取RTU信息失败<br/>";
		    	    txt+="失败原因：";
		    	    txt+="网络错误<br/>";
		    	    txt+="状态码："+XMLHttpRequest.status;
		    	    var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
		    	   	window.wxc.xcConfirm(txt, "info", option);
	        },  
	   });
 }
/***------------------------获取RTU端口号----------------------------*/
 function getRtuPort(rtuId,modelorder){
	 aPortStr=[];
	 aPortOrder=[];
	 $.ajax({
	   	    url:'getPortByRtuIdAndModelName',
	        type:'post', //数据发送方式
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	        	   "addJumperType":$("#addJumperType").val(),
	        	   "rtuId":rtuId,
	        	   "modelOrder":modelorder
	        	 },
	         success: function(data){
	        	 for(var i = 0;i<data[0].portOrder.length;i++){
	        		  aPortOrder.push(data[0].portOrder[i]);
	        		  aPortStr.push(data[0].portName[i]);
	        	}
	        	 if(aPortOrder.length<1){
	        		    var txt="当前模块无用于连接该类跳纤的端口。";
			    	    var option={
			    	   					title: "提示",
			    	   					btn: parseInt("0001",2),
			    	   					onOk: function(){//点击确认的执行方法
			    	   						
			    	  			       }
			    	   				}
			    	   	window.wxc.xcConfirm(txt, "info", option);
	        	 }
	        	 
	         },
	         error:function(XMLHttpRequest,Error){
	        	    var txt="获取配端口信息失败<br/>";
		    	    txt+="失败原因：";
		    	    txt+="网络错误<br/>";
		    	    txt+="状态码："+XMLHttpRequest.status;
		    	    var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
		    	   	window.wxc.xcConfirm(txt, "info", option);
	        },  
	   });
 }
 /***------------------------获取frame端口号----------------------------*/
 function getFramePort(frameId,mark){
	if(mark=="a"){//a 端
		  aPortOrder=[];
		  aPortStr=[];
		}
	 else{
		  zPortOrder=[];
		  zPortStr=[];
		 }
	 /**新建切换跳纤时只显示连接有混合模块上光路的端口**/
	 var portType='all'; 
	 if($("#addJumperType").val()=='切换跳纤'){
		 portType='jumperSwitch';
	 }
	 else if($("#addJumperType").val()=='备纤光源跳纤'){
		 portType='jumperBackup';
	 }
	 else if($("#addJumperType").val()=='光路跳纤'){
		 portType='jumperRoute';
	 }
	 $.ajax({
	   	    url:'getPortByFrameId',
	        type:'post', //数据发送方式
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	        	   "frameId":frameId,
	        	   'portType':portType,
	        	 },
	         success: function(json){
	        	if(!json[0].status){
	        		    var txt="当前配线架不存在可用于您当前选择类型跳纤的端口<br/>";
			    	    var option={
			    	   					title: "提示",
			    	   					btn: parseInt("0001",2),
			    	   					onOk: function(){//点击确认的执行方法
			    	   						
			    	  			       }
			    	   				}
			    	   	window.wxc.xcConfirm(txt, "info", option);
	        	 }
	        	else{
	        		  for(var jsonCount=1;jsonCount<json.length;jsonCount++){
	        			  if(mark=="a"){//a 端
	        				  aPortOrder.push(json[jsonCount].order);
	        				  aPortStr.push(json[jsonCount].name);
	        			  }
	        			  if(mark=="z"){
	        				  zPortOrder.push(json[jsonCount].order);
	        				  zPortStr.push(json[jsonCount].name);
	        			  }
	        			  
	        		  }
	        	}
	         },
	         error:function(XMLHttpRequest,Error){
	        	    var txt="获取配线架端口信息失败<br/>";
		    	    txt+="失败原因：";
		    	    txt+="网络错误<br/>";
		    	    txt+="状态码："+XMLHttpRequest.status;
		    	    var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
		    	   	window.wxc.xcConfirm(txt, "info", option);
	        },  
	   });
 }
 
 /***-----------------------确认增加按钮--------------------------------------*/
 $(".confirm").click(function(){
	var selectStationId=stationId;
	$.ajax({
		    url:'jumper/addJumper',
	        type:'post', //数据发送方式
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	        	   "modelOrder":$("#aRtuModel").val(),
	        	   "stationId":stationId,
	        	   "type":$("#addJumperType").val(),
	        	   "jumperName":$("#jumperName").val(),
	        	   "jumperLength":$("#jumperLength").val(),
	        	   "aEquipmentId":$("#aEquipmentName").val(),
	        	   "aEquipmentName":$("#aEquipmentName").find("option:selected").text(),
	        	   "aPortOrder":$("#aEquipmentPort").val(),
	        	   "zEquipmentId":$("#zEquipmentName").val(),
	        	   "zEquipmentName":$("#zEquipmentName").find("option:selected").text(),
	        	   "zPortOrder":$("#zEquipmentPort").val(),
	        	   "description":$("#description").val(),
	             },
	         success: function(json){
	        	    var txt="";
			    	if(json[0].status){
			    		 $(".sidebarDiv").html("");
			    		txt+="增加成功<br/>"
			    	}
			    	else{
			    		txt+="增加失败，请重试。失败原因："+json[0].err;
			    	}
				     var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
									refreashTable(selectStationId);
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
			      },
			    error:function(XMLHttpRequest,Error)
			      {
			         	$(".sidebarDiv").html("");
			    	    var txt="增加失败<br/>";
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
 
 /**---------------------------刷新显示----------------------------*/
 function refreashTable(selectStationId){
	 var tds = document.querySelectorAll("#jumperTableDetail tbody tr td");
	    for (var i=0;i<tds.length;i++){
	        tds[i].innerHTML = "";
	    }
	    $.ajax({
		    	 url:"getJumperByStationId",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "stationId":selectStationId,           	 
	                  },
				 success: function(json){
					 if(json[0].status)
					  {
						 for(var jsonCount=1;jsonCount<json.length;jsonCount++){
					        	 var jumperArr=[ 
					        	                 jsonCount,
					        	                 json[jsonCount].id,
					        	                 json[jsonCount].name,
					        	                 json[jsonCount].length,
					        	                 json[jsonCount].type,
					        	                 json[jsonCount].equipmentAName,
					        	                 json[jsonCount].equipmentAId,
					        	                 json[jsonCount].equipmentAPort,
					        	                 json[jsonCount].equipmentZName,
					        	                 json[jsonCount].equipmentZId,
					        	                 json[jsonCount].equipmentZPort,
					        	                 json[jsonCount].stationName,
					        	                 json[jsonCount].stationId,
					        	                 json[jsonCount].description,
					        	                 json[jsonCount].createTime,
					        	                 json[jsonCount].alterTime
					        	               ];//跳纤
							setJumperTable(jumperArr);
		
				         }
				     }
				}
		});  
	    
   }
    function setJumperTable(tableData) {      
 	 //  alert("table");
   	   var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
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
 /**-------------------------------------------cancel---------------------------*/
 $(".cancel").click(function(){
     $(".contentDiv").html("");
     $(".sidebarDiv").html("");
 });
 /**-------------------------------------------close---------------------------*/
 $(".sidebar_close").click(function(){
     $(".contentDiv").html("");
     $(".sidebarDiv").html("");
 });
 