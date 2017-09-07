/**-------------------形成左侧树------------------*/
$(document).ready(function () {
	var hrefCable = "javascript:jumperReload()";
	var hrefStaAddress = "";//定义url指向js，当为区域时需要使用
	leftTree = new dTree('leftTree');//创建一个对象.
	leftTree.config.folderLinks = true;
	leftTree.config.useCookies = false;
	leftTree.config.check = true;
	$.ajax({
		url: 'create_tree',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {

			for (var cableJsonCount = 0; cableJsonCount < json.length; cableJsonCount++) {
				var cableStationId = "";//定义字符串来承接stationId
				var cableNodeId = json[cableJsonCount].id;//读取节点id
				var cableNodeName = json[cableJsonCount].name;
				var cableParentId = json[cableJsonCount].pid;//读取节点pid
				if (parseInt(cableNodeId[0]) < 3) {  //显示到局站
					if (parseInt(cableNodeId[0]) == 2) {//为局站
						for (var index = 2; index < cableNodeId.length; index++) { //读取StationId，    id新式为x_xxx,所以从第二位开始为StationId
							cableStationId += cableNodeId[index];//拼接字符串，获得字符串形式的areaId
						}

						hrefStaAddress = "javascript:getJumperByStationId('" + parseInt(cableStationId) + "','" + cableNodeName + "')";//为局站，添加点击事件，function()入口参数为areaId
						// alert(hrefAddress);
						leftTree.add(cableNodeId, cableParentId, cableNodeName, hrefStaAddress, "", "", "", "", false);
					}

					if (cableNodeId == "0" || cableNodeId == 0) {
						leftTree.add(cableNodeId, cableParentId, "跳纤管理", hrefCable, "", "", "", "", true);
					}

					if (parseInt(cableNodeId[0]) == 1) {//区域
						leftTree.add(cableNodeId, cableParentId, cableNodeName, "", "", "", "", "", false);
					}

				}
			}
			// alert(leftTree);
			document.getElementById("leftTree").innerHTML = leftTree;

		},
	});

});



/*--------------------点击跳纤管理节点将会重新载入当前页面-------------------*/
function jumperReload() {

	$(".containerDiv").load("html/resource/jumper.html");
}
/*-------------点击局站，从数据库获得当前局站下所有跳纤，并刷新右侧光缆列表---------------------*/
function getJumperByStationId(stationId, stationName) {
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	localStorage.setItem("jumperStationId", stationId);
	localStorage.setItem("jumperStationName", stationName);
	/*------刷新显示跳纤列表，先清空-------*/
	var tds = document.querySelectorAll("#jumperTableDetail tbody tr td");
	for (var i = 0; i < tds.length; i++) {
		tds[i].innerHTML = "";
	}
	$.ajax({
		    	 url: "getJumperByStationId",
		    	 dataType: 'json', //接受数据格式
		async: false,
		data: {
			"stationId": stationId,
		},
		success: function (json) {
			if (json[0].status) {//status true
				var jumperPara = [];
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
					/*-----------刷新显示当前跳纤的列表----------*/
					jumperPara.push(jumperArr);

				}
				setTable(jumperPara);//实现分页表格写入
			}
		},
		error: function () {
			$(".sidebarDiv").load("html/resource/alertNetworkError.html");
			$(".contentDiv").html("");
		}
	});

}



/*------------------------跳纤----------------------------*/
$("#doSearch").click(function () {
	var searchJumperName = $("#searchJumperName").val();
	var searchJumperType = $("#searchJumperType").val();
	if (searchJumperName == "") {    //未输入查询信息
		var txt = "请输入查询条件<br/>";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);

	}
	else {

		/*------刷新显示地标列表，先清空-------*/
		var tds = document.querySelectorAll("#jumperTableDetail tbody tr td");
		for (var i = 0; i < tds.length; i++) {
			tds[i].innerHTML = "";
		}
		$.ajax({
			url: "searchJumper",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"name": searchJumperName,
				"type": searchJumperType,
			},
			success: function (json) {
				if (json[0].status) {
					var jumperPara = [];
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
					setTable(jumperPara);//实现分页表格写入
				}
				else {    //不存在查找项
					var txt = "查找失败<br/>";
					txt += "当前局站不存在满足要求的跳纤";
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法

						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
			},
			error: function () {
				var txt = "查找失败<br/>";
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
		/*成功与否都将查询输入重置*/
		$("#searchJumperName").val("");
	}

});


/************************实现分页的表格写入函数***************************/
function setTable(tableData) {
	//console.log("**********************jumper表格分页写入")
	var nums = document.querySelectorAll(".jumperTableDetail tbody tr").length;
	var cells = document.getElementById("jumperTableDetail").rows.item(0).cells.length;
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
			var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
			var tds = document.querySelectorAll("#jumperTableDetail tbody tr td");
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



/**表格范围内禁止系统右键，使用自定义右键**/
$("#jumperTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$(".jumperTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var selectId = this.children[1].innerHTML;
	var jumperType = this.children[4].innerHTML;//读取选中的跳纤类型
	if (selectId != "") {
		$(this).addClass("jumper").siblings().removeClass("jumper");
	}
	else{
		 $(this).removeClass("jumper").siblings().removeClass("jumper");

	}
    var menu = new BootstrapMenu('.jumper', {
		actions: [{
			name: '修改跳纤',
			onClick: function () {
				$(this).remove();	
				$(".sidebarDiv").load("html/resource/jumperModify.html");
			}
		},
		{
			name: '删除跳纤',
			onClick: function () {
				$(this).remove();
				var txt = "警告：删除操作不可恢复！";
				switch(jumperType){
				   case"光路跳纤":
				   case"配线架跳纤":
					    txt+="删除当前跳纤将删除所属光路及光路所在保护组。"
				   break;
				   case"切换跳纤":
					    txt+="删除当前跳纤将删除光路所在保护组。"
				   break;
				   default:
				   break;
				}
				txt += "请谨慎选择，";
				txt += "确认删除定点击\"确定键\"。";
				var option = {
					title: "提示",
					btn: parseInt("0011", 2),
					onOk: function () {//点击确认的执行方法
						delJumper(selectId);
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}]
	});
})
/**------------------------增加跳纤----------------------------*/
$(".jumperAdd").click(function () {
	if (localStorage.getItem("jumperStationId") == null)//未进入局站
	{
		var txt = "您尚未进入任何局站<br/>";
		txt += "清先点击左侧菜单树进入局站<br/>";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else
		$(".sidebarDiv").load("html/resource/jumperAdd.html");
});
/*------------------------修改跳纤----------------------------*/
$(".jumperModify").click(function () {
	   var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
	var selectFlag = 0;
	for (var tableCount = 0; tableCount < trs.length; tableCount++) {
		if ($(trs[tableCount]).hasClass("currtr")) {
			if (trs[tableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
		}
	}
	if (selectFlag == 0)//未选择
	{
		var txt = "请先选择您要修改的跳纤<br/>";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		$(".sidebarDiv").load("html/resource/jumperModify.html");
	}

});

$(".jumperDelete").click(function () {
	   var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
	   var selectId = "";
	   var jumperType="";
	for (var i = 0; i < trs.length; i++) {
		if ($(trs[i]).hasClass("currtr")) {
			selectId = trs[i].children[1].innerHTML;
			jumperType = trs[i].children[4].innerHTML;//读取选中的跳纤类型
			i = trs.length;
		}
	}
	if (selectId == "") {
		var txt = "请先选择您要删除的跳纤";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	   else {
		var txt = "警告：删除操作不可恢复!";
		switch(jumperType){
		   case"光路跳纤":
		   case"配线架跳纤":
			    txt+="删除当前跳纤将删除所属光路及光路所在保护组。"
		   break;
		   case"切换跳纤":
		   case"备纤光路跳纤":
			    txt+="删除当前跳纤将删除光路所在保护组。"
		   break;
		   default:
		   break;
		}
		txt += "请谨慎选择<br/>";
		txt += "确认删除请定点击\"确定键\"。"
		var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				delJumper(selectId);
			}
		}
		window.wxc.xcConfirm(txt, "info", option);

	}
});
/**删除跳纤**/
function delJumper() {
	var trs = document.querySelectorAll("#jumperTableDetail tbody tr");
	var jumperId = 0;
	var jumperType = null;
	for (var i = 0; i < trs.length; i++) {
		if ($(trs[i]).hasClass("currtr")) {
			jumperId = parseInt(trs[i].children[1].innerHTML);//读取选中的跳纤ID
			jumperType = trs[i].children[4].innerHTML;//读取选中的跳纤类型
		}
	}
	if (jumperType == "配线架跳纤") {
		$.ajax({
			url: 'jumper/delJumperFrame',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"jumperId": jumperId
			},
			success: function (json) {
				var txt = "";
				if (json[0].status) {
					txt += "删除成功<br/>"
				}
				else {
					txt += "删除失败，请重试";
				}
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
						getJumperByStationId(localStorage.getItem("jumperStationId"), localStorage.getItem("jumperStationName"));
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
			},

		});
	}
	else if(jumperType == "备纤光源跳纤"){

		$.ajax({
			url: 'jumper/delJumperBackup',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"jumperId": jumperId
			},
			success: function (json) {
				var txt = "";
				if (json[0].status) {
					txt += "删除成功<br/>"
				}
				else {
					txt += "删除失败，失败原因："+json[0].err;
				}
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
						getJumperByStationId(localStorage.getItem("jumperStationId"), localStorage.getItem("jumperStationName"));
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
			},

		});
	
	}
	else {
		$.ajax({
			url: 'jumper/delJumperRoute',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"jumperId": jumperId
			},
			success: function (json) {
				var txt = "";
				if (json[0].status) {
					txt += "删除成功<br/>"
				}
				else {
					txt += "删除失败，失败原因："+json[0].err;
				}
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
						getJumperByStationId(localStorage.getItem("jumperStationId"), localStorage.getItem("jumperStationName"));
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
			},

		});
	}
}
/**GIS相关**/
//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display", "none");
$(".mapDiv").css("display", "block");

$("#backToGis").click(function () {
	$(".rightTable").css("display", "none");
	$(".mapDiv").css("display", "block");
})

function openSpeficTree(stationId) {
	   leftTree.config.folderLinks = false;
	   $(".rightTable").css("display", "block");
	   $(".mapDiv").css("display", "none");
	   leftTree.closeAll();
	var stationid = "2_" + stationId;
	leftTree.openTo(stationid, true);
}