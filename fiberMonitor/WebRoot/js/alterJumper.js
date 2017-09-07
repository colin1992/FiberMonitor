/**
 * 
 */
var selectStationId = 0;
$(document).ready(function () {
	var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
	var jumperPara = [];
	for (var i = 0; i < trs.length; i++) {
		if ($(trs[i]).hasClass("currtr")) {
			jumperPara = [
				trs[i].children[1].innerHTML,
				trs[i].children[2].innerHTML,
				trs[i].children[3].innerHTML,
				trs[i].children[13].innerHTML,
				trs[i].children[12].innerHTML,
				trs[i].children[4].innerHTML,
			];
		}
	}
	$("#jumperId").val(jumperPara[0]);//id
	$("#jumperName").val(jumperPara[1]);//name
	$("#jumperLength").val(jumperPara[2]);//length
	$("#description").val(jumperPara[3]);//description
	$("#type").val(jumperPara[5]);//
	selectStationId = parseInt(jumperPara[4]);
});

/*---------------------------------close按键----------------------------------------------*/
$(".sidebar_close").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");
});

/*---------------------------------确定按键----------------------------------------------*/
$(".confirm").click(function () {

	$.ajax({
		url: 'jumper/modifyJumper',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"jumperId": $("#jumperId").val(),
			"jumperName": $("#jumperName").val(),
			"jumperLength": $("#jumperLength").val(),
			"description": $("#description").val(),
			"type": $("#type").val(),
		},
		success: function (json) {
			$(".sidebarDiv").html("");
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
					refreashTable();;
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
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

});

/*---------------------------刷新显示----------------------------*/
function refreashTable() {
	var tds = document.querySelectorAll("#jumperTableDetail tbody tr td");
	for (var i = 0; i < tds.length; i++) {
		tds[i].innerHTML = "";
	}
	$.ajax({
		url: "getJumperByStationId",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"stationId": selectStationId,
		},
		success: function (json) {
			if (json[0].status) {
				var jumperPara=[];
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					var jumperArr = [
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
					jumperPara.push(jumperArr);

				}
				setTable(jumperPara);//
			}
		}
	});

}
/*-------------------------------------------cancel---------------------------*/
$(".cancel").click(function () {
	$(".contentDiv").html("");
	$(".sidebarDiv").html("");
});