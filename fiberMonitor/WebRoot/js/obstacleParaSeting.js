/**
 * 
 */
/**----------公共变量-----------*/
    var rtuId=0;
	var routeId=0;
	var groupId=0;
	var type=0;
	var portOrder=0;
	var routeArray=[];
	var orderAndTypes=[];
/**--------初始化--------*/
$(document).ready(function(){
	    var url = document.location.href.split("//")[1];//通过读取浏览器地址栏来获取构建webSocket的地址
	    url =url.split(":")[0];
	    var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
	    if(!(ip.test(url)&&(RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256))){
    		/**如果从浏览器获取到的不是IP地址，查询数据库**/
	    	$.ajax({
	    		type: 'post', //数据发送方式
	    		dataType: 'json', //接受数据格式
	    		async: false,
	    		url: 'getServerIp',
	    		data: '',
	    		success: function (json) {
	    			if (json[0].status) {
	    				url=json[0].ip;
	    			}
	    		}
	    	})
     }
	    $("#P19").val(url);
	    $("#P18").val(url);
	
	
	 // 为validator添加 IP地址验证  方法
    jQuery.validator.addMethod("ip", function(value, element) {    
    	var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    	return this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
    	}, "ip地址格式错误");
	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
    for (var i=0;i<trs.length;i++){
    	 if($(trs[i]).hasClass("currtr")&&trs[i].children[1].innerHTML!=""){
    		 routeId=trs[i].children[1].innerHTML;
    		 rtuId=trs[i].children[4].innerHTML;
    		 groupId=trs[i].children[14].innerHTML;
    		 portOrder=getPortOrder(trs[i].children[6].innerHTML);// rtu order
    		 type=0;
    		 if(trs[i].children[12].innerHTML=="在线纤"){//status
    			 type=1;
    		 }
    		 i=trs.length;
	    	}
    }
    routeArray.push(parseInt(routeId));
    orderAndTypes.push([portOrder,type]);
	var P11=[1,5,10,30,60,100,180];
	var P12=[
			    		  [10,20,40,80,160],
			    		  [20,40,80,160,320],
			    		  [40,80,160,320,640],
			    		  [80,160,320,640,1280],
			    		  [160,320,640,1280,2560],
			    		  [640,1280,2560,5120,10240],
			    		  [1280,2560,5120,10240,20480]
	  		];
	$("#SNo").val(routeId);
	for (var count = 0; count < P11.length; count++) {
		if (count == 2) {
			$("#P11").append("<option value=" + P11[count] + " selected='selected'>" + P11[count] + "</option>");
		}
		else {
			$("#P11").append("<option value=" + P11[count] + ">" + P11[count] + "</option>");
		}
	}
	for (var index = 0; index < P12[2].length; index++) {
		if (index == 0) {
			$("#P12").append("<option value=" + P12[2][index] + " selected='selected'>" + P12[2][index] + "</option>");
		}
		else {
			$("#P12").append("<option value=" + P12[2][index] + ">" + P12[2][index] + "</option>");
		}
	}

	$("#P11").change(function () {
		var range = $(this).prop('selectedIndex');
		$("#P12").empty();
		for (var index = 0; index < P12[range].length; index++) {
			if (index == 0) {
				$("#P12").append("<option value=" + P12[range][index] + " selected='selected'>" + P12[range][index] + "</option>");
			}
			else {
				$("#P12").append("<option value=" + P12[range][index] + ">" + P12[range][index] + "</option>");
			}
		}
	})
})
/**----------------应用到同组所有光路勾选变化---------------*/
$("#isUseToAll").change(function(){
	if($("#isUseToAll").is(':checked')){//一组
		routeArray=[];
		orderAndTypes=[];
		var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
	    for (var i=0;i<trs.length;i++){
	    	     var thisRouteId=trs[i].children[1].innerHTML;
	    		 var thisGroupId=trs[i].children[14].innerHTML;
	    		 var thisRtuOrder=getPortOrder(trs[i].children[6].innerHTML);// rtu order
	    		 var thisType=0;
	    		 if(trs[i].children[12].innerHTML=="在线纤"){//status
	    			 thisType=1;
	    		 }
	    		 if((groupId==thisGroupId)&&(groupId!="")){
	    			 routeArray.push(parseInt(thisRouteId));
	    			 orderAndTypes.push([thisRtuOrder,thisType]);
	 		     }
	    }
	    var routeIdString="";
	    for(var index=0;index<routeArray.length;index++){
	    	routeIdString+=routeArray[index];
	    	if(index!=routeArray.length-1){
	    		routeIdString+=",";
	    	}
	    }
	    if(routeIdString!=""){
	    	$("#SNo").val(routeIdString);
	    }
	    if($("#isUseOpti").is(':checked')){//如果已经勾选了使用优化餐数，检查是否具有优化测试参数
	    	 status=checkOpticalPara(routeArray);
	    }
	}
	else{//一条
		routeArray=[];
		orderAndTypes=[];
		routeArray.push(parseInt(routeId));
		orderAndTypes.push([portOrder,type]);
		$("#SNo").val(routeId);
	}
	
})

/**----------------采用优化测试参数所有勾选变化---------------*/
$("#isUseOpti").change(function(){
	if($("#isUseOpti").is(':checked')){
		 if(!($("#isUseOptiSpan").text()=="设置为优化参数"))
		    checkOpticalPara(routeArray);
	    }
	else{
		abledPara();
	}
	   
	
})
/**---------检查选择的光路是否具有优化测试参数----------*/
   function checkOpticalPara(routeArray){
	 $.ajax({
		 url:"checkOpticalPara",
         type:'post', //数据发送方式
         dataType:'json', //接受数据格式
         async: false,
         data:{
       	        "routeIdS": JSON.stringify(routeArray),
               },
		  success: function(json){
			      {
			    	if(json[0].status){
			    		var unSetRoutes=json[0].unSetRoutes;
			    		  var txt= "您选择的光路中存在尚未配置优化测试参数的光路<br/>";
			    		  txt+="如要进行当前操作您需要先为光路号为";
			    		  for(var count=0;count<unSetRoutes.length;count++){
			    			  txt+=unSetRoutes[count];
			    			  if(count!=unSetRoutes.length-1){
			    				  txt+=",";
			    			  }
			    		  }
			    		  txt+="的光路设置优化测试参数!";
			    		  txt+="<br/>您也可以为本次测试配置参数并将之作为优化测试参数!";
			    		  var option = {
									title: "提示",
									btn: parseInt("0001",2),
									onOk: function(){//点击确认的执行方法
										$("#isUseOptiSpan").text("设置为优化参数");
									}
								}
						  window.wxc.xcConfirm(txt, "info", option);
			    	}
			    	else{
			    		 getOptical(routeId);//获取优化测试参数
			    		 disabledPara();//禁止输入
			    	}
				  }
			  }
         })
        
}
/**---------获取优化参数-------*/
 function getOptical(routeId){
	 $.ajax({
		 url:"getOpticalPara",
         type:'post', //数据发送方式
         dataType:'json', //接受数据格式
         async: false,
         data:{
       	        "routeId":routeId,
               },
		  success: function(json){
			      if(json[0].status){
			    		var optiPara=json[0].optiPara;
			    		 for(var index=1;index<=7;index++){
			    			  var itemId="#P1";
			    			  itemId+=index;
			    			  $(itemId).val(optiPara[index-1]); 
			    		 }
			    	}
			 }
         })
 } 
/**----------禁止参数框----------*/
 function disabledPara(){
	 for(var index=1;index<=7;index++){
		  var itemId="#P1";
		  itemId+=index;
		  $(itemId).attr("disabled",true); 
	 }
 }
 /**----------使能参数框----------*/
 function abledPara(){
	 for(var index=1;index<=7;index++){
		  var itemId="#P1";
		  itemId+=index;
		  $(itemId).attr("disabled",false); 
	 }
 }
/**----------close-----------*/
 $(".sidebar_close").click(function(){
        	$(".contentDiv").html("");
            $(".sidebarDiv").html("");
        });
 /**----------cancel-----------*/
 $(".cancel").click(function(){
        	$(".contentDiv").html("");
            $(".sidebarDiv").html("");
        });
 /**表单验证*/
 function validateObstacleForm() {
		return $("#obstacleParaForm").validate({
			rules: {
				th1: {
					required: true,
					number: true
				},
				th2: {
					required: true,
					number: true
				},
				th3: {
					required: true,
					number: true
				},
				th4: {
					required: true,
					number: true
				},
				th5: {
					required: true,
					number: true
				},
				th6: {
					required: true,
					number: true
				},
				th7: {
					required: true,
					number: true
				},
				th8: {
					required: true,
					number: true
				}
			},
			messages: {
				th1: {
					required: "请输全程传输损耗门限",
					number: "请输入适当数字"
				},
				th2: {
					required: "请输光学长度门限",
					number: "请输入适当数字"
				},
				th3: {
					required: "请输接头损耗门限",
					number: "请输入适当数字"
				},
				th4: {
					required: "请输两点离散发射门限",
					number: "请输入适当数字"
				},
				th5: {
					required: "请输接头间衰减门限",
					number: "请输入适当数字"
				},
				th6: {
					required: "请输光功率参考门限",
					number: "请输入适当数字"
				},
				th7: {
					required: "请输二次告警间隔",
					number: "请输入适当数字"
				},
				th8: {
					required: "请输重发告警间隔",
					number: "请输入适当数字"
				}
			}
		}).form();
	}
 function validateOptiForm() {
		return $("#optiForm").validate({
			rules: {
				P14: {
					required: true,
					digits: true
				},
				P15: {
					required: true,
					range: [1, 2]
				},
				P16: {
					required: true,
					number: true
				},
				P17: {
					required: true,
					number: true
				},
				P18: {
					required: true,
					ip: true
				},
				P19: {
					required: true,
					ip: true
				}
			},
			messages: {
				P14: {
					required: "请输入平均次数",
					digits: "请输入整数"
				},
				P15: {
					required: "请输入折射率,范围为1-2",
					range: "请输入1~2的数字"
				},
				P16: {
					required: "请输入非反射门限",
					number: "请输入适当数字"
				},
				P17: {
					required: "请输入结束门限",
					number: "请输入适当数字"
				},
				P18: {
					required: "请输入非反射门限",
				},
				P19: {
					required: "请输入结束门限",
				}
			}
		}).form();
	}
 /**-------confirm----------*/
  $(".confirm").click(function(){
	  if(validateObstacleForm()&&validateOptiForm()){
		  var threshodList=getGroupList();
			$.ajax({
				url:"curve/obstacleTest/setObstacle",
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		      	        "CM":rtuId,
		      	        "threshodList":JSON.stringify(threshodList),
		              },
				  success: function(json){
					    if(json[0].status){
					    	  var txt= "配置信息已成功下发到,当前光路已启动障碍告警测试";
							  var option = {
										title: "提示",
										btn: parseInt("0001",2),
										onOk: function(){//点击确认的执行方法
											//refreashTable();
											getRouteByRtuId(rtuId);
									 }
									}
							  window.wxc.xcConfirm(txt, "info", option);
						  }
						  else{
							   var txt= "配置失败<br/>";
							   txt+="失败原因："+json[0].err;    
						       var option = {
										title: "提示",
										btn: parseInt("0001",2),
										onOk: function(){//点击确认的执行方法
											//refreashTable();
											getRouteByRtuId(rtuId);
										}
									}
							  window.wxc.xcConfirm(txt, "info", option);
						  }
					  },
					  error:function(XMLHttpRequest,textStatus){
						    var txt="配置失败<br/>";
				  			txt+="失败原因：";
				  			if(XMLHttpRequest.status==401){
				  			    txt+="您不具有当前操作的权限<br/>";
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
										//refreashTable();//刷新表格
										getRouteByRtuId(rtuId);
									}
								}
						  window.wxc.xcConfirm(txt, "info", option);
						},
		        })
			  
		 	 $(".contentDiv").html("");
		     $(".sidebarDiv").html("");  
	  }
 });
  /**--------------获取一个告警组的光路配置信息--------------*/
  function getGroupList(){
	  var threshodList=[];//一个告警组的各条光路
	  for(var routeIndex=0;routeIndex<orderAndTypes.length;routeIndex++){
		  var threshodMap=[];//一条光路的参数信息
		  threshodMap.push(orderAndTypes[routeIndex][0]);//SNo
		  for(var index=1;index<7;index++){//6个门限
			  var itemId="#th";
			  itemId+=index;
			  threshodMap.push($(itemId).val()); 
		  }
		  threshodMap.push(orderAndTypes[routeIndex][1]);//type
		  threshodMap.push($("#P18").val());//IP01
		  for(var ipIndex=2;ipIndex<7;ipIndex++){//IP02-IP06
			  threshodMap.push($("#P19").val());
		  }
		  threshodMap.push($("#th7").val());//T3
		  threshodMap.push($("#th8").val());//T4
		  var PS=1;
		  if($("#isUseOpti").is(':checked')){
			  PS=0;
		  }
		  threshodMap.push(PS);//PS
		  threshodMap.push($("#P11").find("option:selected").text());
		  for(var index=2;index<8;index++){
			  var itemId="#P1";
			  itemId+=index;
			  threshodMap.push($(itemId).val()); 
		 } 
		 threshodList.push(threshodMap);//在组中加入当前光路的配置信息
		 
	  }
	  return [groupId,threshodList];//组号和光路参数信息
  }
  /**------------刷新显示表格---------------*/
  function refreashTable(){
 	  var trs = document.querySelectorAll("#obstacleTableDetail tbody tr td");
 	    for (var i=0;i<trs.length;i++){
 		    trs[i].innerHTML = "";
 		}//先清空
 	    $.ajax({
 		    	 url:"getObstacleRouteByRtuId",
 		    	 dataType:'json', //接受数据格式
 	             async: false,
 	             data:{
 	                     "id":rtuId,           	 
 	                  },
 				 success: function(json){
 					 if(json[0].status){
 						 var tds = document.querySelectorAll("#obstacleTableDetail tbody tr td");
 					 	    for (var i=0;i<tds.length;i++){
 					 	        tds[i].innerHTML = "";
 					 	    }
 					           var routePara=[];
 							   for(var count=1;count<json.length;count++){
 								   var equiPara=[
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
 								  
 								  routePara.push(equiPara);   
 							   }
 							   setTable(routePara);
 					   }
 			         
 				 },
 			 
 	     });
   }
   
  
