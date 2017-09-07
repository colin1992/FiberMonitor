/**
 * 
 */
/****定时器**/
var timerForCallTest = null;
/**----------------------全局变量---------------------------------*/
/**当前选择的光路Id**/
var routeId=0;
/**当前选中的光路名**/
var routeName="";
var trs = document.querySelectorAll("#routeTableDetail tbody tr");
for (var i = 0; i < trs.length; i++) {
	if ($(trs[i]).hasClass("currtr")) {
		routeId = trs[i].children[1].innerHTML;
		routeName=trs[i].children[2].innerHTML;
		i=trs.length;
	}       

}
$("#SNo").val(routeId);
$("#curveName").val(routeName);

/***
 * 设置优化参数 check box勾选变化
 * **/
$("#isUseOpti").change(function(){
	if($("#isUseOpti").is(':checked')){
		 if($("#isUseOptiSpan").text()=="采用优化参数")
			 getOptical(routeId);
	    }
})
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
			      else{
			    	    $("#isUseOptiSpan").text("设为优化参数");
			    	    $("#isUseOpti").val(false);
			    	    var txt = "当前光路尚未配置优化参数，您可以在勾选\"设为优化参数\"项将本次测试的参数设为优化参数。";
						var option = {
							title: "提示",
							btn: parseInt("0001", 2),
							onOk: function () {//点击确认的执行方法
								
							}
						}
						window.wxc.xcConfirm(txt, "info", option);
			      }
			 }
         })
 } 
$(".sidebar_close").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");

});
/**------------------------点名测试取消操作------------------------------*/
$(".cancelTesting").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");

});

/**----------------------------开始点名测试按钮后验证表单-------------------------------------------*/
function validateForm() {
	return $("#callTestingConForm").validate({
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
			}
		},
		messages: {
			P14: {
				required: "请输入平均次数",
				digits: "请输入整数"
			},
			P15: {
				required: "请输入折射率",
				range: "请输入1~2的数字"
			},
			P16: {
				required: "请输入非反射门限",
				number: "请输入适当数字"
			},
			P17: {
				required: "请输入结束门限",
				number: "请输入适当数字"
			}
		}
	}).form();
}
$(".startTesting").click(function () {
	if (validateForm()) {
		var trs = document.querySelectorAll("#routeTableDetail tbody tr");
		var data = [];
		var PS = 1;
		var P14 = $("#P14").val();
		var P15 = $("#P15").val();
		var P16 = $("#P16").val();
		var P17 = $("#P17").val();
		var P11 = $("#P11").val();
		var P12 = $("#P12").val();
		var P13 = $("#P13").val();
		$(".sidebar").html("");
		$(".sidebar").load("html/curve/waitText.html");//进入等待页面
		$.ajax({
			url: "curve/callTest",//服务器地址
			type: "POST",//采用POST请求
			dataType: "json",//否则用Text或其他
			timeout: (parseInt(P14)*1.5+12) * 1000,
			data: {
				'routeId':routeId,
				'PS': PS,
				'P11': P11,
				'P12': P12,
				'P13': P13,
				'P14': P14,
				'P15': P15,
				'P16': P16,
				'P17': P17,
			},
			success: function (json) {
				if (json[0].status) {//由于localStorage只能存字符串，从而先将数组转换为字符串然后进行存储，在读取数据时再将字符串还原为数组
					listRoutesByRtuId(json[0].rtuId);//测试成功后刷新页面  因为有可能存在切换的情景,函数定义在callTesting.js中
					localStorage.setItem('curveId', json[0].curveId);
					loadCurve();
				}
				else {
					var txt = "测试失败,失败原因：" + json[0].err;
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法
							clearInterval(timerForCallTest);
							timerForCallTest = null;
							$(".sidebarDiv").html("");
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
			},
			error: function (XMLHttpRequest, Error) {
				clearInterval(timerForCallTest);
				timerForCallTest = null;
				$(".sidebarDiv").html("");
				var txt = "测试失败<br/>";
				txt += "失败原因：";
				if (XMLHttpRequest.status == 401) {
					txt += "您不具有当前操作的权限<br/>";
				}
				else if (XMLHttpRequest.status == 0) {
					txt += "连接超时";
				}
				else {
					txt += "网络错误<br/>";
					txt += "状态码：" + XMLHttpRequest.status;
				}
				var option = {
					  			 title: "提示",
					  			 btn: parseInt("0001", 2),
					  			 onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		});
	}
});

/**-------------------------跳转到曲线分析界面--------------------------------------*/
function loadCurve() {
	clearInterval(timerForCallTest);
	timerForCallTest = null;
	$(".sidebarDiv").html("");
	
	$(".dynamicSupernatant").height(document.documentElement.clientHeight);
	$(".dynamicSupernatant").width(document.documentElement.clientWidth);
	$(".dynamicSupernatant").show();
	$("#supernatantArea").css({ "width": "95%", "height": "90%", "backgroundColor": "#f9f9f9", "margin": "0 auto", "positon": "absolute", "margin-top": "-2%" });
	$("#supernatant").height($("#supernatantArea").height()-30);
	$("#supernatant").width("99.6%");
	$("#supernatant").attr("src", "html/curve/curveAnalyze.html");
	

}
