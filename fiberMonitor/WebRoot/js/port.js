/**
 * 
 */
/**
 * landmark main js
 */
/*-----------------------形成左侧树------------------*/
$(document).ready(function () {
	var hrefCable = "javascript:Reload()";
	var hrefStaAddress = "";//定义url指向js，当为区域时需要使用
	leftTree = new dTree('leftTree');//创建一个对象.
	leftTree.config.folderLinks = true;
	leftTree.config.useCookies = false;
	leftTree.config.check = true;
	$.ajax({
		url: 'portTree',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {

			for (var cableJsonCount = 0; cableJsonCount < json.length; cableJsonCount++) {

				var equiId = "";//定义字符串来承接stationId
				var NodeId = json[cableJsonCount].id;//读取节点id
				var nodeName = json[cableJsonCount].name;
				var parentId = json[cableJsonCount].pid;//读取节点pid
				if (parseInt(NodeId[0]) < 4) {  //显示到局站
					if (NodeId == "0" || NodeId == 0) {
						leftTree.add(NodeId, parentId, "端口管理", hrefCable, "", "", "", "", true);
					}
					else if (parseInt(NodeId[0]) == 3) {//为RTU或配线架
						if ((NodeId[1] == "0") || (NodeId[1] == 0)) {//配线架
							for (var index = 3; index < NodeId.length; index++) { //
								equiId += NodeId[index];//拼接字符串
							}
							hrefStaAddress = "javascript:getFramePort('" + parseInt(equiId) + "')";//
						}
						else {  //为RTU
							for (var index = 2; index < NodeId.length; index++) { //
								equiId += NodeId[index];//拼接字符串
							}
							hrefStaAddress = "javascript:getRtuPort('" + parseInt(equiId) + "')";//
						}

						leftTree.add(NodeId, parentId, nodeName, hrefStaAddress, "", "", "", "", false);
					}


					else {
						leftTree.add(NodeId, parentId, nodeName, "", "", "", "", "", false);
					}

				}
			}

			document.getElementById("leftTree").innerHTML = leftTree;

		},
		error: function (XMLHttpRequest, Error) {
			alert("访问出错了！！" + "\n请求状态：" + XMLHttpRequest.status + "\n错误原因：" + Error);

		}
	});

});



/*--------------------点击光缆port管理节点将会重新载入当前页面-------------------*/
function Reload() {
	$(".containerDiv").load("html/resource/port.html");
}
/*-------------点击frame，从数据库获得所有端口---------------------*/
function getFramePort(frameId) {
	   $(".rightTable").css("display", "block");
	   $(".mapDiv").css("display", "none");
	   var trs = document.querySelectorAll("#portTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	}//先清空
	var portData = [];
	$.ajax({
		    	 url: "getFramePortByFrameId",
		    	 dataType: 'json', //接受数据格式
		async: false,
		data: {
			"id": frameId,
		},
		success: function (json) {
			if (json[0].status) {
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					equiPara = [
						json[jsonCount].id,
						json[jsonCount].name,
						json[jsonCount].order,
						json[jsonCount].equiType,
						json[jsonCount].equiId,
						json[jsonCount].equiName,
						json[jsonCount].status,
						json[jsonCount].description,
						json[jsonCount].alterUser,
						json[jsonCount].alterTime
					];
					portData.push(equiPara);

				}
				showTable(portData);
			}

		},

	});

}




/*-------------点击RTU，从数据库获得所有端口---------------------*/
function getRtuPort(rtuId) {
	   $(".rightTable").css("display", "block");
	   $(".mapDiv").css("display", "none");
	   var trs = document.querySelectorAll("#portTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	}//先清空
	var portData = [];
	$.ajax({
		    	 url: "getRtuPortByRtuId",
		    	 dataType: 'json', //接受数据格式
		async: false,
		data: {
			"id": rtuId,
		},
		success: function (json) {
			if (json[0].status) {
				var portData = [];
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					var equiPara = [
						json[jsonCount].id,
						json[jsonCount].name,
						json[jsonCount].order,
						json[jsonCount].equiType,
						json[jsonCount].equiId,
						json[jsonCount].equiName,
						json[jsonCount].status,
						json[jsonCount].description,
						json[jsonCount].alterUser,
						json[jsonCount].alterTime
					];
					portData.push(equiPara);

				}
				showTable(portData);
			}

		},

	});

}

/************************实现分页的表格写入函数***************************/
function showTable(tableData) {
	var nums = document.querySelectorAll(".portTableDetail tbody tr").length;
	var cells = document.getElementById("portTableDetail").rows.item(0).cells.length;
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
			var trs = document.querySelectorAll("#portTableDetail tbody tr");
			var tds = document.querySelectorAll("#portTableDetail tbody tr td");
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
$("#portTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$(".portTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var selectId = this.children[1].innerHTML;
	if (selectId != "") {
		$(this).addClass("port").siblings().removeClass("port");
	}
	var menu = new BootstrapMenu('.port', {
		actions: [{
			name: '修改端口',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/resource/portModify.html");
			}
		},
		{
			name: '查看端口',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/resource/portCheck.html");
			}
		}]
	});
})

/*----------------------查询端口------------------------------*/
$("#doSerch").click(function () {
	var id = $("#searchId").val();
	var name = $("#searchName").val();
	if ((id == "") && (name == "")) {    //未输入查询信息
		var txt = "查询条件不能为空，请先输入查询条件";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {

		/*------刷新显示列表，先清空-------*/
		var tds = document.querySelectorAll("#portTableDetail tbody tr td");
		for (var i = 0; i < tds.length; i++) {
			tds[i].innerHTML = "";
		}
		$.ajax({
			url: "getPortByMulti",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"id": id,
				"name": name,
			},
			success: function (json) {
				if (json[0].status) {//查找到数据
					if (json[0].status) {
						var portPara = [];
						for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
							var equiPara = [
								json[jsonCount].id,
								json[jsonCount].name,
								json[jsonCount].order,
								json[jsonCount].equiType,
								json[jsonCount].equiId,
								json[jsonCount].equiName,
								json[jsonCount].status,
								json[jsonCount].description,
								json[jsonCount].alterUser,
								json[jsonCount].alterTime
							];
							portPara.push(equiPara);
						}
						showTable(portPara);
					}

				}
				else {    //不存在查找项
					var txt = "查找失败,当前局站不存在满足条件的端口";
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

				var txt = "查找失败<br/>";
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
		/*成功与否都将查询输入重置*/
		$("#searchId").val("");
		$("#searchName").val("");
	}

});

/*----------------------查询重置------------------------------*/
$("#reset").click(function () {
	$("#searchId").val("");
	$("#searchName").val("");
});



/*--------------------------修改端口-----------------------------*/
$(".portAlter").click(function () {
	var trs = document.querySelectorAll("#portTableDetail tbody tr");
	var selectFlag = 0;
	for (var tableCount = 0; tableCount < trs.length; tableCount++) {
		if ($(trs[tableCount]).hasClass("currtr")) {
			if (trs[tableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
			{
				selectFlag = 1;
				tableCount = trs.length;

			}

		}
	}
	if (selectFlag == 0)//未选择port
	{
		var txt = "请先选择您要修改的端口";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {

		$(".sidebarDiv").load("html/resource/portModify.html");
	}
});
/*--------------------------查看-----------------------------*/
$(".portDetial").click(function () {
	var trs = document.querySelectorAll("#portTableDetail tbody tr");
	var selectFlag = 0;
	for (var tableCount = 0; tableCount < trs.length; tableCount++) {
		if ($(trs[tableCount]).hasClass("currtr")) {
			if (trs[tableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
		}
	} if (selectFlag == 0)//未选择cable
	{
		var txt = "请先选择您要查看的端口";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		$(".sidebarDiv").load("html/resource/portCheck.html");
	}
});
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
	leftTree.closeAll();
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	var stationid = "2_" + stationId;
	leftTree.openTo(stationid, false);
}