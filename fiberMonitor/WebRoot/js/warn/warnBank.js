/**
 * 告警经验库js
 */
/**检查session**/
$(document).ready(function(){
	checkSession();
	getAllWarnExperience();
})
/************************实现分页的表格写入函数***************************/
function setAlarmBankTable(tableData) {
	var nums = document.querySelectorAll(".warningBankTableDetail tbody tr").length;
	var cells = document.getElementById("warningBankTableDetail").rows.item(0).cells.length;
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
			var trs = document.querySelectorAll("#warningBankTableDetail tbody tr");
			var tds = document.querySelectorAll("#warningBankTableDetail tbody tr td");
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

/**获取所有的告警经验信息*/

function getAllWarnExperience() {
	var tds = document.querySelectorAll("#warningBankTableDetail tbody tr td");
	for (var i = 0; i < tds.length; i++) {
		tds[i].innerHTML = "";
	}
	$.ajax({
		    url: "getAllWarnExperience",
		    dataType: 'json', //接受数据格式
		async: false,
		data: {},
		success: function (json) {
			if (json[0].status) {
				var bank=json[0].banks;
				var warnBank = [];
				for (var i =0; i< bank.length; i++) {
					var warn = [
						bank[i].id,
						bank[i].source,
						bank[i].type,
						bank[i].level,
						bank[i].experience,
						bank[i].createTime,
						bank[i].createUser,
						bank[i].alterTime,
						bank[i].alterUser,
					];
					warnBank.push(warn);
				}
				setAlarmBankTable(warnBank);//实现分页的表格写入函数
			}
			else{
				var txt = "系统目前不存在告警经验，您可以点击\"新增经验\"按键添加告警经验<br/>";
				var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}
	});

}
/**新增告警经验**/
$("#createWarn").click(function(){
	$(".sidebarDiv").load("html/warn/bankAdd.html");
})
/**表格范围内禁止系统右键，使用自定义右键**/
$("#warningBankTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$("#warningBankTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var selectId = this.children[1].innerHTML;
	if (selectId != "") {
		$(this).addClass("warnBank").siblings().removeClass("warnBank");
	}
	 else{//不存在条目
		 $(this).removeClass("warnBank").siblings().removeClass("warnBank");
	 }
	var menu = new BootstrapMenu('.warnBank', {
		actions: [{
			name: '修改',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/warn/bankModify.html");
			}
		},
		{
			name: '删除',
			onClick: function () {
				$(this).remove();
				var txt = "警告：删除操作不可恢复！";
				txt += "请谨慎选择,";
				txt += "确认删除定点击\"确定\"键。"
				var option = {
					title: "提示",
					btn: parseInt("0011", 2),
					onOk: function () {//点击确认的执行方法
						delWarnBank(selectId);
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}]
	});
})
/**删除经验**/
function delWarnBank(id) {
	$.ajax({
		url: 'alarm/delAlarmBank',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"warnBankId": id
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {
				txt += "删除成功<br/>"
			}
			else {
				txt += "删除失败,请重试。";
			}
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
					getAllWarnExperience();
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
		},
		error: function (XMLHttpRequest, Error) {

			var txt = "删除失败<br/>";
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
/**----------------------修改-------------------------*/
$(".cableModify").click(function () {

	var trs = document.querySelectorAll("#warningBankTableDetail tbody tr");
	var selectFlag = 0;
	for (var cableTableCount = 0; cableTableCount < trs.length; cableTableCount++) {
		if ($(trs[cableTableCount]).hasClass("currtr")) {
			if (trs[cableTableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
			   cableTableCount=trs.length;
		}
	} if (selectFlag == 0)//未选择cable
	{
		var txt = "请先选择您要修改的处理经验。";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	} else {
		$(".sidebarDiv").load("html/warn/bankModify.html");

	}
});
/**-----------------------------------删除---------------------------*/
$("#deleteWarn").click(function () {
	var trs = document.querySelectorAll("#warningBankTableDetail tbody tr");
	var selectId = "";
	for (var cableTableCount = 0; cableTableCount < trs.length; cableTableCount++) {
		if ($(trs[cableTableCount]).hasClass("currtr")) {
			selectId = trs[cableTableCount].children[1].innerHTML;
			cableTableCount=trs.length;
		}
	} if (selectId == "")//未选择cable
	{
		var txt = "请先选择您要删除的处理经验。";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		var txt = "警告：删除操作不可恢复，请谨慎操作。<br/>";
		txt += "确认删除定点击确定键"
		var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				delWarnBank(selectId);
			}
		}
		window.wxc.xcConfirm(txt, "info", option);

	}
});
/**----------------------------------查找---------------------------*/
$("#search").click(function () {
	var clc=[];
	setAlarmBankTable(clc);//先清空
	$.ajax({
		url: 'searchAlarmBank',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"type":$("#type").val(),
	    	"source":$("#source").val(),
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {
				var bank=json[0].banks;
				var warnBank = [];
				for (var i =0; i< bank.length; i++) {
					var warn = [
						bank[i].id,
						bank[i].source,
						bank[i].type,
						bank[i].level,
						bank[i].experience,
						bank[i].createTime,
						bank[i].createUser,
						bank[i].alterTime,
						bank[i].alterUser,
					];
					warnBank.push(warn);
				}
				setAlarmBankTable(warnBank);//实现分页的表格写入函数
			}
			else {
				txt += "查找失败,不存在满足条件的条目。";
				var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法
							
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
			}
			
		},
		error: function (XMLHttpRequest, Error) {

			var txt = "查找失败,失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限<br/>";
			}
			else {
				txt="查找失败,不存在满足条件的条目。";
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
