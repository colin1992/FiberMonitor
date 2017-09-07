/**
 * cable main
 */
/**全局变量**/
/**定义一个全局变量来记录当前页面原则的局站的ID**/


var currentStationId = 0;



/*--------------------------形成左侧树----------------------------*/
$(document).ready(function () {
	tree = new dTree('tree');//创建一个对象.
	tree.config.folderLinks = true;
	tree.config.useCookies = false;
	tree.config.check = true;
	$.ajax({
		url: 'create_tree',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {
			var hrefStation = "javascript:stationReload()";
			for (var count = 0; count < json.length; count++) {
				var stationId = " ";//定义字符串来承接areaId
				var nodeId = json[count].id;//读取节点id
				var areaId = "";
				// alert("nodeId:"+nodeId);
				var parentId = json[count].pid;//读取节点pid
				var stationName = json[count].name;
				var hrefAddress = "";//定义url指向js，当为区域时需要使用
				if (parseInt(json[count].id[0]) < 3) {  //显示到局站
					if (parseInt(json[count].id[0]) == 2) {//为局站
						for (var index = 2; index < json[count].id.length; index++) { //读取StationId，    id新式为x_xxx,所以从第二位开始为StationId
							stationId += json[count].id[index];//拼接字符串，获得字符串形式的areaId

						}
						for (var pidCount = 2; pidCount < parentId.length; pidCount++) {
							areaId += parentId[pidCount];
						}

						hrefAddress = "javascript:getCableByStationId('" + parseInt(stationId) + "','" + stationName + "','" + areaId + "')";//为局站，添加点击事件，function()入口参数为areaId
					}
					var nodeName = json[count].name;
					if (nodeId == "0" || nodeId == 0) {
						tree.add(nodeId, parentId, "光缆管理", hrefStation, "", "", "", "", true);
					} else if (nodeId[0] == "2") {   //局站id为  2_xx
						tree.add(nodeId, parentId, nodeName, hrefAddress, "", "", "", "", false);
					}
					else {//区域
						tree.add(nodeId, parentId, nodeName, "", "", "", "", "", false);
					}

				}
			}


		},
	});
	document.getElementById("leftTree").innerHTML = tree;

});


/*点击根节点，重载当前页面*/
function stationReload() {
	localStorage.setItem("thisAreaId", 0);
	$(".containerDiv").load("html/resource/cable.html");
}

/************************实现分页的表格写入函数***************************/
function setTable(tableData) {
	// console.log("**********************局站表格分页写入")
	var nums = document.querySelectorAll(".cableTableDetail tbody tr").length;
	var cells = document.getElementById("cableTableDetail").rows.item(0).cells.length;
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
			var trs = document.querySelectorAll("#cableTableDetail tbody tr");
			var tds = document.querySelectorAll("#cableTableDetail tbody tr td");
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


/**通过局站id获取与该局站相关的所有光缆*/

function getCableByStationId(stationId) {
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	currentStationId = stationId; //每次点击一次局站都要更新一下currentStation的值  
	var tds = document.querySelectorAll("#cableTableDetail tbody tr td");
	for (var i = 0; i < tds.length; i++) {
		tds[i].innerHTML = "";
	}
	$.ajax({
		    	 url: "getCablesByStationId",
		    	 dataType: 'json', //接受数据格式
		async: false,
		data: {
			"stationId": stationId,
		},
		success: function (json) {
			if (json[0].status) {
				var cablePara = [];
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					var cableData = [
						json[jsonCount].id,
						json[jsonCount].name,
						json[jsonCount].Length,
						json[jsonCount].aPortName,
						json[jsonCount].aRemainLength,
						json[jsonCount].zPortName,
						json[jsonCount].zRemainLength,
						json[jsonCount].networkLevel,
						json[jsonCount].laidWay,
						json[jsonCount].coreStrct,
						json[jsonCount].coreNumber,
						json[jsonCount].refractiveIndex,
						json[jsonCount].description,
						json[jsonCount].createTime,
						json[jsonCount].createUser,
//						json[jsonCount].alterTime,
//						json[jsonCount].alterUser

					];

					cablePara.push(cableData);

				}
				setTable(cablePara);//实现分页的表格写入函数
			}
		}
	});

}


/**表格范围内禁止系统右键，使用自定义右键**/
$("#cableTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$(".cableTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var selectId = this.children[1].innerHTML;
	if (selectId != "") {
		$(this).addClass("cable").siblings().removeClass("cable");
	}
	 else{//不存在条目
		 $(this).removeClass("cable").siblings().removeClass("cable");
	 }
	var menu = new BootstrapMenu('.cable', {
		actions: [{
			name: '修改光缆',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/resource/cableModify.html");
			}
		},
		{
			name: '删除光缆',
			onClick: function () {
				$(this).remove();
				var txt = "警告：删除光缆将删除其下的所有资源！";
				txt += "请谨慎选择，";
				txt += "确认删除定点击确定键。"
				var option = {
					title: "提示",
					btn: parseInt("0011", 2),
					onOk: function () {//点击确认的执行方法
						delCable(selectId);
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}]
	});
})
/*----------------------查询光缆------------------------------*/
$("#doCableSerch").click(function () {
	var cableName = $("#searchCableName").val();
	var networkLevel = $("#searchNewworkLevel").val();
	if ((cableName == "") && (networkLevel == "index")) {    //未输入查询信息
		$(".sidebarDiv").load("html/resource/alertInputSearchItem.html");
		$(".contentDiv").html("");
	}
	else {
		/*------刷新显示，先清空-------*/
		var tds = document.querySelectorAll("#cableTableDetail tbody tr td");
		for (var i = 0; i < tds.length; i++) {
			tds[i].innerHTML = "";
		}
		$.ajax({
			url: "searchCable",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"cableName": cableName,
				"networkLevel": networkLevel,
			},
			success: function (json) {
				if (json[0].status) {//查找到数据
					var cablePara = [];
					for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
						var cableData = [

							json[jsonCount].id,
							json[jsonCount].name,
							json[jsonCount].Length,
							json[jsonCount].aPortName,
							json[jsonCount].aRemainLength,
							json[jsonCount].zPortName,
							json[jsonCount].zRemainLength,
							json[jsonCount].networkLevel,
							json[jsonCount].laidWay,
							json[jsonCount].coreStrct,
							json[jsonCount].coreNumber,
							json[jsonCount].refractiveIndex,
							json[jsonCount].description,
							json[jsonCount].createTime,
							json[jsonCount].createUser,
//							json[jsonCount].alterTime,
//							json[jsonCount].alterUser

						];

						cablePara.push(cableData);

					}
					setTable(cablePara);//cablePara
				}
				else {    //不存在查找项
					$(".sidebarDiv").html("");
					var txt = "查找失败，不存在查找项";
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
				$(".sidebarDiv").html("");
				var txt = "查找失败，不存在查找项";
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
		/*无论查询成功与否都将查询重置*/
		$("#searchLaidWay").val("index");//下拉列表回到初始态
		$("#searchCableName").val("");
		$("#searchNewworkLevel").val("index");
	}

});

/*----------------------查询重置------------------------------*/
$("#clearcableSearch").click(function () {
	$("#searchLaidWay").val("index");//下拉列表回到初始态
	$("#searchCableName").val("");
	$("#searchNewworkLevel").val("index");

});

/*----------------------新增光缆-------------------------*/
$(".cableAdd").click(function () {
	$(".sidebarDiv").load("html/resource/cableAdd.html");
});
/*----------------------修改光缆-------------------------*/
$(".cableModify").click(function () {

	var trs = document.querySelectorAll("#cableTableDetail tbody tr");
	var selectFlag = 0;
	for (var cableTableCount = 0; cableTableCount < trs.length; cableTableCount++) {
		if ($(trs[cableTableCount]).hasClass("currtr")) {
			if (trs[cableTableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
			    cableTableCount=trs.length;
		}
	} if (selectFlag == 0)//未选择cable
	{
		var txt = "请先选择您要修改的光缆";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	} else {
		$(".sidebarDiv").load("html/resource/cableModify.html");

	}
});
/**-----------------------------------删除光缆---------------------------*/
$(".cableDelete").click(function () {
	var trs = document.querySelectorAll("#cableTableDetail tbody tr");
	var selectId = "";
	for (var cableTableCount = 0; cableTableCount < trs.length; cableTableCount++) {
		if ($(trs[cableTableCount]).hasClass("currtr")) {
			selectId = trs[cableTableCount].children[1].innerHTML;
			cableTableCount=trs.length;
		}
	} if (selectId == "")//未选择cable
	{
		var txt = "请先选择您要删除的光缆";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		var txt = "警告：删除光缆将删除其下的所有资源！<br/>";
		txt += "请谨慎选择<br/>";
		txt += "确认删除定点击确定键"
		var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				delCable(selectId);
			}
		}
		window.wxc.xcConfirm(txt, "info", option);

	}
});
function delCable(cableId) {
	$.ajax({
		url: 'cable/delCable',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"cableId": cableId
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {
				txt += "删除成功<br/>"
			}
			else {
				txt += "删除失败,失败原因："+json[0].err;
			}
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
					getCableByStationId(currentStationId);
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

//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display", "none");
$(".mapDiv").css("display", "block");

$("#backToGis").click(function () {
	$(".rightTable").css("display", "none");
	$(".mapDiv").css("display", "block");
})
/**点击GIS局站，展开树**/
function openSpeficTree(stationId) {
	tree.config.folderLinks = false;
	tree.closeAll();
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	var stationid = "2_" + stationId;
	tree.openTo(stationid, true);
}
