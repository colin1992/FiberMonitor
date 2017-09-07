
/**----------公共变量-----------*/
var rtuId = 0;
var routeId = 0;
var portOrder = 0;
/**--------初始化--------*/
$(document).ready(function () {
	var trs = document.querySelectorAll("#routeTableDetail tbody tr");
	var routeName = "";
	for (var i = 0; i < trs.length; i++) {
		if ($(trs[i]).hasClass("currtr")) {
			routeId = trs[i].children[1].innerHTML;
			routeName =trs[i].children[2].innerHTML;
			portOrder =getPortOrder(trs[i].children[5].innerHTML);// rtu order
			/**通过光路Id获取CM**/
			getRtuIdByRouteId(routeId);
			//点名测试和光路设置都采用了同意个页面设置优化测试参数，但是表格内容不同，加以区分
			if (isNaN(parseInt(portOrder))) {//不是数字，说明是点名测试页面设置参数
				portOrder =getPortOrder(trs[i].children[4].innerHTML);// rtu order
				routeId = trs[i].children[1].innerHTML;
				routeName = trs[i].children[2].innerHTML;
				rtuId = trs[i].children[10].innerHTML;
			}
		}
	}
	var P11 = [1, 5, 10, 30, 60, 100, 180];
	var P12 = [
				[10, 20, 40, 80, 160],
				[20, 40, 80, 160, 320],
				[40, 80, 160, 320, 640],
				[80, 160, 320, 640, 1280],
				[160, 320, 640, 1280, 2560],
				[640, 1280, 2560, 5120, 10240],
				[1280, 2560, 5120, 10240, 20480]
	];
	$("#SNo").val(routeId);
	$("#curveName").val(routeName);
	for (var count = 0; count < P11.length; count++) {
		if (count == 2) {
			$("#P11").append("<option value=" +P11[count] + " selected='selected'>" + P11[count] + "</option>");
		}
		else {
			$("#P11").append("<option value=" +P11[count] + ">" + P11[count] + "</option>");
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
/**getRouteIdByRouteId**/
function getRtuIdByRouteId(routeId){
	$.ajax({
		url: "route/getRtuIdByRouteId",
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"routeId": routeId,
		},
		success: function (json) {
			if (json[0].status) {
				rtuId=json[0].rtuId;
			}
			
		},
		error: function (XMLHttpRequest, textStatus, F) {}
	});
}
/**输入框只能是数字**/
$(".onlyNum").focus(function(){
	   $(this).keypress(function (event) {
           var eventObj = event || e;
           var keyCode = eventObj.keyCode || eventObj.which;
           if ((keyCode >= 45 && keyCode <= 57)&&(keyCode!=47))
               return true;
           else
               return false;
	  	})
   })

/**=======确认======*/
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
$(".confirm").click(function () {
	if (validateForm()) {
		var optiPara = [];
		var optiaclP = [];
		optiaclP.push(portOrder);
		optiaclP.push($("#P11").find("option:selected").text());
		for (var index = 2; index <= 7; index++) {
			var itemId = "#P1";
			itemId += index;
			optiaclP.push($(itemId).val());
		}
		optiPara.push(optiaclP);
		$.ajax({
			url: "route/optiRoute",
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"CM": rtuId,
				"setCount": optiPara.length,
				"optiPara": JSON.stringify(optiPara),
			},
			success: function (json) {
				if (json[0].status) {
					var txt = "优化参数配置成功！\n";
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
				else {
					var txt = "配置失败\n";
					txt += "错误原因：" + json[0].err;
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
			},
			error: function (XMLHttpRequest, textStatus, F) {
				var txt = "操作失败<br/>";
				txt += "失败原因：";
				if (XMLHttpRequest.status == 401) {
					txt += "您不具有当前操作的权限<br/>";
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
		$(".contentDiv").html("");
		$(".sidebarDiv").html("");
	}
});

/*=======cancel======*/
$(".cancel").click(function () {
	$(".sidebarDiv").html("");
	$(".contentDiv").html("");
});
/**/
$(".sidebar_close").click(function () {
	$(".sidebarDiv").html("");
	$(".contentDiv").html("");
});