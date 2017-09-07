/**
 * 
 */
/*----------公共变量-----------*/
    var rtuId=0;
	var routeId=0;
	var portOrder=0;		
/*--------初始化--------*/
$(document).ready(function(){
	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
	var routeName="";
    for (var index=0;index<trs.length;index++){
    	 if($(trs[index]).hasClass("currtr")){
    		 routeId=trs[index].children[1].innerHTML;
    		 routeName=trs[index].children[2].innerHTML;
    		 rtuId=trs[index].children[4].innerHTML;
    		 portOrder=getPortOrder(trs[index].children[6].innerHTML);// rtu order
    		 index=trs.length;
    	}
    }
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
	$("#thisRouteName").val(routeName);
	for (var count = 0; count < P11.length; count++) {
		if (count == 2) {
			$("#P11").append("<option value=" + count + " selected='selected'>" + P11[count] + "</option>");
		}
		else {
			$("#P11").append("<option value=" + count + ">" + P11[count] + "</option>");
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
/*=======确认======*/
$(".confirm").click(function(){
	var optiPara=[];
	var optiaclP=[];
	optiaclP.push(portOrder);
	optiaclP.push($("#P11").find("option:selected").text());
	 for(var index=2;index<=7;index++){
		  var itemId="#P1";
		  itemId+=index;
		  optiaclP.push($(itemId).val()); 
		  
	 } 
	optiPara.push(optiaclP);
	$.ajax({
		url:"route/optiRoute",
        type:'post', //数据发送方式
        dataType:'json', //接受数据格式
        async: false,
        data:{
      	        "CM":rtuId,
      	        "setCount":optiPara.length,
      	        "optiPara":JSON.stringify(optiPara),
              },
		  success: function(json){
			    if(json[0].status){
			    	  var txt= "优化参数配置成功！";
					  var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
							  }
							}
					  window.wxc.xcConfirm(txt, "info", option);
				  }
				  else{
					   var txt= "配置失败\n";
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
				  var txt= "配置失败，网络错误\n";
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
	  
 	 $(".contentDiv").html("");
     $(".sidebarDiv").html("");
});

/*=======cancel======*/
$(".cancel").click(function(){
	 $(".sidebarDiv").html(""); 
     $(".contentDiv").html("");
});
/**/
$(".sidebar_close").click(function(){
	 $(".sidebarDiv").html(""); 
     $(".contentDiv").html("");
});