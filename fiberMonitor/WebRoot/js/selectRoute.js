/**
 * 当用户设置完局站信息后，下发相关信息，后台返回可用光路，由用户选择并设置基本信息
 */
/*----------------全局变量--------------------*/
var routeToplogics=JSON.parse(localStorage.getItem('topological'));
var routeNames=JSON.parse(localStorage.getItem('route'));
var preparatoryId=JSON.parse(localStorage.getItem('preparatoryId'));
var framePortOrder=JSON.parse(localStorage.getItem('framePortOrder'));
var crossStation=JSON.parse(localStorage.getItem('crossStation'));
var cosNumber=JSON.parse(localStorage.getItem('crossStation'))[0].length-2;
/**-------------设置select list的值-------------------------*/
$(document).ready(function(){
	$("#routeListSelect").empty();
	/*value的值为routeToplogics对应的序号，比如value=0表示第一条光路的top， top为二维数组，top[0]为topologicalRoute，top[1]为topologicalPoint*/
	for(var count=0;count<routeNames.length;count++){
		$("#routeListSelect").append("<option value="+count+">"+routeNames[count]+"</option>");//
	}
	var routeP=[];
	localStorage.setItem('routeSetedPara', JSON.stringify(routeP));	
})
/**----------获取所有选择的value-----------*/
function getChecked(){
    var checkedValues=$("#routeListSelect").multiselect("MyValues");
	return checkedValues;
	
}


/**----------------------点击选项----------------------*/
/** event: the original event object ui.value: value of the checkbox 
 * ui.text: text of the checkbox 
 * ui.checked: whether or not the input was checked or unchecked (boolean) */ 
$("#routeListSelect").on("multiselectclick", function(event, ui) { 
	  if(ui.checked){
		  localStorage.setItem("routeLogic",ui.text);
		  $(".routeLi").css('display','none');
		  $(".sidebarRouteDiv").css('display','block');
		  $(".sidebarRouteDiv").load("routeParaSet.html");
		  var value=parseInt(ui.value);//获取当前的value
		  localStorage.setItem("selectValue",value);
	  }
	});

$(".sidebar_close").click(function(){
	$(".preRouteList").html("");
});

$(".confirm").click(function(){
	 var allPara=JSON.parse(localStorage.getItem("routeSetedPara"));
	 var allSelectValues=getChecked();
	 for(var lCount=0;lCount<allPara.length;lCount++){
		  var isExit=false;
		  for(var sCount=0;sCount<allSelectValues.length;sCount++){
			  if(parseInt(allPara[lCount][0])==parseInt(allSelectValues[sCount])){
				  isExit=true;
			  }
		  }
	   if(!isExit){
		   allPara.splice(index,1);
		   lCount--;
	   }	  
	  }
	  var allRoute=[];
	  var preId=[];
	  var corssS=[];
	  var frameOrder=[];
	  for(var count=0;count<allPara.length;count++){
		  var oneRoutePara=[];
		  var thisValue=parseInt(allPara[count][0]);
		  allPara[count].shift();//将序号移除
		  allPara[count].push(cosNumber)//加入跨段数目  局站数减2
		  oneRoutePara=[allPara[count],routeToplogics[thisValue]];
		  allRoute.push(oneRoutePara);
		  preId.push(preparatoryId[thisValue]);//预备光路Id
		  frameOrder.push(framePortOrder[thisValue]);//Z端配线架端口号
		  /**全部的参数    allRoute[i][0]为一维数组，内容是用户设定的光路参数    
		   *  allRoute[i][1]为二纬数组， allRoute[i][1][0]topologicalRoute
		   *  allRoute[i][1][1]topologicalPoint
		   */
	  }
	  
	  setRoute(allRoute,preId,frameOrder);//设置光路
});
/**-------------------下发设置光路------------------------*/
function setRoute(para,preId,frameOrder){
	/**-------para的结构为
	  * para[0] route name
	  * para[1] route description
	  * para[2] route isOnline
	  * para[3] route 链路方向，上行链路或下行链路
	  * para[4] route crossNumber
	  * -------*/
	 var preIdStr=JSON.stringify(preId);
	  $(".preRouteList").html("");  
	  $.ajax({
		url:"../../route/addRoute",
		dataType:'json', //接受数据格式
        async: false,
        data:{
                "routePara":JSON.stringify(para),  //下发的为三维数组   
                "prepId":preIdStr,
                "frameOrder":JSON.stringify(frameOrder),
                "crossStation":JSON.stringify(crossStation),
             },
		success: function(json){
			if(json[0].status){//设置成功
				var rtuId=parseInt(json[0].rtuId);//获取回传的RTU
				var txt= "您已成功设置当前光路<br/>";
			    var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								parent.getRouteByRtuId(rtuId);//刷新光路，方法在route.js中
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
			}
			 else{  //设置失败
				  var txt= "设置失败，请重试<br/>";
				  txt+="失败原因："+json[0].err;
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
			  var txt= "设置失败，网络错误<br/>";
			  txt+="状态码："+XMLHttpRequest.status+"<br/>";
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

   
