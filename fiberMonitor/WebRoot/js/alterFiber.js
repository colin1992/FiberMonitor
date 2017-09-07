/*-----------------------全局变量-------------*/
var stationAId = localStorage.getItem("stationAId");
var stationZId = localStorage.getItem("stationZId");
var cableId = 0;
var oldFrameAId = 0;
var oldFrameAPortOrder = 0;
var oldFrameBId = 0;
var oldFrameBPortOrder = 0;
/**----------------页面载入后读取表格数据------------------------------*/
$(document).ready(function () {
	var trs = document.querySelectorAll("#fiberTableDetail tbody tr");
	var fiberPara = [];
	for (var i = 0; i < trs.length; i++) {
		if ($(trs[i]).hasClass("currtr")) {
			for (var count = 1; count < 17; count++)//读取欲取的值
				fiberPara.push(trs[i].children[count].innerHTML);
		}
	}
	oldFrameAId = fiberPara[9];
	oldFrameAPortOrder = fiberPara[11];
	oldFrameBId = fiberPara[13];
	oldFrameBPortOrder = fiberPara[15];
	cableId = fiberPara[4];
	$("#fiberId").val(fiberPara[0]);//id
	$("#fiberName").val(fiberPara[1]);//name
	$("#length").val(fiberPara[2]);//
	$("#reIndex").val(fiberPara[3]);//
	$("#description").val(fiberPara[16]);//description
	getFrame(stationAId, "a");
	getFrame(stationZId, "z");
	if(fiberPara[13] != ""){
		$("#frameZ").attr("disabled",true);
	}
	if(fiberPara[9] != ""){
		$("#frameA").attr("disabled",true);
	}
	if(fiberPara[11] != ""){
		$("#frameAPort").attr("disabled",true);
	}
	if(fiberPara[15] != ""){
		$("#frameZPort").attr("disabled",true);
	}
	if (fiberPara[9] != "") {
		$("#frameA option[value='index']").remove(); 
		$("#frameA").val(fiberPara[9]);//
		$("#frameA").attr("readonly","readonly");
		getFramePort(parseInt(fiberPara[9]), "a");  //得到的端口不包括被当前这个光钎占用的端口
		var currentPortNameA = "端口";      //////////////// 
		currentPortNameA = currentPortNameA + fiberPara[11];///////////当前这个端口的名字
		$("#frameAPort").append("<option value=" + fiberPara[11] + ">" + currentPortNameA + "</option>");
		if(fiberPara[11]!=""){
			$("#frameAPort").val(fiberPara[11]);//默认选中当前端口
		}
	}
	else {
		if ($("#frameA").val("index") != "err") {//有配线架
			$("#frameA").val("index");//
			$("#frameAPort").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		}

	}
	if (fiberPara[13] != "") {
		$("#frameZ option[value='index']").remove();
		$("#frameZ").val(fiberPara[13]);
		getFramePort(parseInt(fiberPara[13]), "z");
		var currentPortNameB = "端口";
		currentPortNameB = currentPortNameB + fiberPara[15];
		$("#frameZPort").append("<option value=" + fiberPara[15] + ">" + currentPortNameB + "</option>");//当前端口放在下拉框中
		$("#frameZPort").val(fiberPara[15]);//
	}
	else {
		if ($("#frameZ").val("index") != "err") {
			$("#frameZ").val("index");//
			$("#frameZPort").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		}
	}
});

/**----------------获取配线架-------------------*/
function getFrame(stationId,flag) {
	var frameId = [];
	var frameName = [];
	var framePara = [];
	$.ajax({
		url: 'getFrameByStationId',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"stationId": stationId,
		    	 },
		success: function (json) {
			if (!json[0].status) {  //无配线架
				if (flag == "a") {
					$("#frameA").empty();
					$("#frameAPort").empty();
					$("#frameA").append("<option value=" + "err" + ">" + "该局站无配线架" + "</option>");//
					$("#frameAPort").append("<option value=" + "err" + ">" + "无配线架端口" + "</option>");//

				}
				else {
					$("#frameZ").empty();
					$("#frameZPort").empty();
					$("#frameZ").append("<option value=" + "err" + ">" + "该局站无配线架" + "</option>");//
					$("#frameZPort").append("<option value=" + "err" + ">" + "无配线架端口" + "</option>");//
				}
			}
			else {
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					frameId.push(json[jsonCount].id);
					frameName.push(json[jsonCount].name);
				}
				framePara = [frameId, frameName];
				//alert(framePara);
				setSelect(framePara, flag);
			}
		},
		error: function () {
			$(".sidebarDiv").load("html/resource/alertNetworkError.html");
			$(".contentDiv").html("");
		},
	});
}

/**-------------设置frame下拉框---------------------*/
function setSelect(framePara, flag) {
	if (flag == "a")//配线架a
	{
		$("#frameA").empty();
		$("#frameA").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		for (var Index = 0; Index < framePara[0].length; Index++) {
			$("#frameA").append("<option value=" + framePara[0][Index] + ">" + framePara[1][Index] + "</option>");//
		}
	}
	else if (flag == "z") {
		$("#frameZ").empty();
		$("#frameZ").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		for (var Index = 0; Index < framePara[0].length; Index++) {
			$("#frameZ").append("<option value=" + framePara[0][Index] + ">" + framePara[1][Index] + "</option>");//
		}
	}
}
/**--------------frameA选择改变--------------*/
$('#frameA').change(function () {
	$("#frameA option[value='index']").remove();
	var frameId = parseInt($("#frameA").val());
	getFramePort(frameId, "a");
})

/**--------------frameZ选择改变--------------*/
$('#frameZ').change(function () {
	$("#frameZ option[value='index']").remove();
	var frameId = parseInt($("#frameZ").val());
	getFramePort(frameId, "z");
})

/**--------------frameAPort选择改变--------------*/
$('#frameAPort').change(function () {
	$("#frameAPort option[value='index']").remove();
})

/**--------------frameZPort选择改变--------------*/
$('#frameZPort').change(function () {
	$("#frameZPort option[value='index']").remove();
})
/**------------------------获取frame端口号----------------------------*/
function getFramePort(frameId, flag) {
	var portId = [];
	var portName = [];
	var portPara = [];
	$.ajax({
		url: 'getPortByFrameId',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"fiber": 1,
			"frameId": frameId,
		},
		success: function (json) {
			if (!json[0].status) {
				$(".sidebarDiv").html("");
				var txt = "当前配线架不存在可用端口<br/>";
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
			else {
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					portId.push(json[jsonCount].order);
					portName.push(json[jsonCount].name);
				}
				portPara = [portId, portName];
				setPortSelect(portPara, flag);
			}
		},
		error: function () {
			$(".sidebarDiv").html("");
			var txt = "获取当前配线架端口信息失败<br/>";
			txt += "失败原因：";
			txt += "网络错误<br/>";
			txt += "状态码：" + XMLHttpRequest.status;
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
		},
	   });
}

/**-------------设置port下拉框---------------------*/
function setPortSelect(framePara, flag) {
	if (flag == "a")//配线架a
	{
		$("#frameAPort").empty();
		$("#frameAPort").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		for (var Index = 0; Index < framePara[0].length; Index++) {
			$("#frameAPort").append("<option value=" + framePara[0][Index] + ">" + framePara[1][Index] + "</option>");//
		}
	}
	else if (flag == "z") {
		$("#frameZPort").empty();
		$("#frameZPort").append("<option value=" + "index" + ">" + "请选择" + "</option>");//
		for (var Index = 0; Index < framePara[0].length; Index++) {
			$("#frameZPort").append("<option value=" + framePara[0][Index] + ">" + framePara[1][Index] + "</option>");//
		}
	}
}

/**---------------------------------close按键----------------------------------------------*/
$(".sidebar_close").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");
});

/**---------------------------------确定按键----------------------------------------------*/

function validateForm() {
	return $("#modifyFiberForm").validate({
		rules: {
			length: {
				required: true,
				number: true,
				min: 0
			},
			reIndex: {
				required: true,
				range: [1, 2]
			}
		},
		messages: {
			length: {
				required: "请输入纤芯长度",
				number: "请输入非负数",
				min: "请输入非负数"
			},
			reIndex: {
				required: "请输入纤芯折射率",
				range: "请输入1~2的数字"
			}
		}
	}).form();
}

$(".confirm").click(function () {
	if (validateForm()) {
		$.ajax({
			url: 'fiber/modifyFiber',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"id": $("#fiberId").val(),
				"name": $("#fiberName").val(),
				"length": $("#length").val(),
				"refractive_index": $("#reIndex").val(),
				"frameAId": $("#frameA").val(),
				"frameAName": $("#frameA").find("option:selected").text(),
				"frameAOrder": $("#frameAPort").val(),
				"frameZId": $("#frameZ").val(),
				"frameZName": $("#frameZ").find("option:selected").text(),
				"frameZOrder": $("#frameZPort").val(),
				"description": $("#description").val(),
				"oldFrameAId": oldFrameAId,        //////////////////
				"oldFrameAPortOrder": oldFrameAPortOrder,/////////////////
				"oldFrameBId": oldFrameBId,          /////////////////
				"oldFrameBPortOrder": oldFrameBPortOrder, ///////////////////
			},
			success: function (json) {
				$(".sidebarDiv").html("");
				if (json[0].status) {
					/*----------刷新显示------------*/
					var txt = "";
					if (json[0].status) {
						txt += "修改成功<br/>"
					}
					else {
						txt += "修改失败，请重试";
					}
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法
							getFiberByCableId(cableId);
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
			},
			error: function (XMLHttpRequest, Error) {
				$(".sidebarDiv").html("");
				var txt = "修改失败<br/>";
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
	}
});

/**--------------------getFiberByCableId-----------------------*/
function getFiberByCableId(id) {
	 	 var trs = document.querySelectorAll("#fiberTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	 		}//先清空
	$.ajax({
		url: "getFiberByCableId",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"id": id,
		},
		success: function (json) {
			if (json[0].status) {
				var allEquiPara = [];
				var tds = document.querySelectorAll("#fiberTableDetail tbody tr td");
				for (var i = 0; i < tds.length; i++) {
					tds[i].innerHTML = "";
				}
				localStorage.setItem("stationAId", parseInt(json[1].stationAId));
				localStorage.setItem("stationZId", parseInt(json[1].stationZId));
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {

					var equiPara = [];
					for (var key in json[jsonCount]) {//遍历json对象的每个key/value对,p为key
						if (equiPara.length < 18) {
							equiPara.push(json[jsonCount][key]);
						}
					}
					allEquiPara.push(equiPara);
				}
				showTable(allEquiPara);
			}
		},
	});
}

/************************实现分页的表格写入函数***************************/
function showTable(tabledata) {    //传入数据为二位数组
	var tableData = tabledata;
	var nums = document.querySelectorAll(".fiberTableDetail tbody tr").length;
	var cells = document.getElementById("fiberTableDetail").rows.item(0).cells.length;
	var pages = Math.ceil(tableData.length / nums);
	var thisData = function (curr) {
		var tableDataCurr = [];
		var last = curr * nums - 1;
		last = last >= tableData.length ? (tableData.length - 1) : last;
		tableDataCurr = tableData.slice(curr * nums - nums, last + 1);
		return tableDataCurr;
	};

	laypage({
		cont: 'pageon',
		pages: pages,
		jump: function (obj) {
			var currTableData = thisData(obj.curr);
			var trs = document.querySelectorAll("#fiberTableDetail tbody tr");
			var tds = document.querySelectorAll("#fiberTableDetail tbody tr td");
			for (var i = 0; i < tds.length; i++) {
				tds[i].innerHTML = "";
			}
			for (var i = 0; i < currTableData.length; i++) {
				var Ele = trs[i].children;
				for (var j = 0; j < cells - 1; j++) {
					Ele[0].innerHTML = i + 1;
					Ele[j + 1].innerHTML = currTableData[i][j];
				}
			}
		}
	});
}

/**-------------------------------------------cancel---------------------------*/
$(".cancel").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");
});