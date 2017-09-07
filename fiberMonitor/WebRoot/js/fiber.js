/**
 * 
 */
$(document).ready(function () {
	var hrefReload = "javascript:Reload()";
	var hrefStaAddress = "";//定义url指向js，当为区域时需要使用
	leftTree = new dTree('leftTree');//创建一个对象.
	leftTree.config.folderLinks = true;
	leftTree.config.useCookies = false;
	leftTree.config.check = true;
	$.ajax({
		url: 'fiberTree',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {
			for (var cableJsonCount = 0; cableJsonCount < json.length; cableJsonCount++) {
				var equiId = "";//定义字符串来承接stationId
				var NodeId = json[cableJsonCount].id;//读取节点id
				var nodeName = json[cableJsonCount].name;
				var parentId = json[cableJsonCount].pid;//读取节点pid
				if (parseInt(NodeId[0]) < 4) {  //显示到光缆
					if (NodeId == "0" || NodeId == 0) {
						leftTree.add(NodeId, parentId, "纤芯管理", hrefReload, "", "", "", "", true);
					}
					else if (parseInt(NodeId[0]) == 3) {//为光缆
						for (var index = 2; index < NodeId.length; index++) { //
							equiId += NodeId[index];//拼接字符串
						}
						hrefStaAddress = "javascript:getFiberByCableId(" + parseInt(equiId) + ")";//
						leftTree.add(NodeId, parentId, nodeName, hrefStaAddress, "", "", "", "", false);
					}

					else {
						leftTree.add(NodeId, parentId, nodeName, "", "", "", "", "", false);
					}
				}

			}
			document.getElementById("leftTree").innerHTML = leftTree;
		},
	});

});


/*--------------------点击光缆port管理节点将会重新载入当前页面-------------------*/
function Reload() {
	$(".containerDiv").load("html/resource/fiber.html");
}


/**表格范围内禁止系统右键，使用自定义右键**/
$("#fiberTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$(".fiberTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var selectId = this.children[1].innerHTML;
	if (selectId != "") {
		$(this).addClass("fiber").siblings().removeClass("fiber");
	}
	else{
		$(this).removeClass("fiber").siblings().removeClass("fiber");
	}
	var menu = new BootstrapMenu('.fiber', {
		actions: [{
			name: '修改纤芯',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/resource/fiberModify.html");
			}
		}]
	});
})


/**--------------------getFiberByCableId-----------------------*/
function getFiberByCableId(cableId) {
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	var trs = document.querySelectorAll("#fiberTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	}//先清空
	$.ajax({
		    	 url: "getFiberByCableId",
		    	 dataType: 'json', //接受数据格式
				async: false,
				data: {
					"id": cableId,
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
/*----------------------查询光纤------------------------------*/
$(".doSearch").click(function () {
	var name = $("#name").val();
	if(name=="") {    //未输入查询信息
		var txt="请先输入查询条件";
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
		var tds = document.querySelectorAll("#fiberTableDetail tbody tr td");
		for (var i = 0; i < tds.length; i++) {
			tds[i].innerHTML = "";
		}
		$.ajax({
			url: "getFiberByMulti",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"name": name,
			},
			success: function (json) {
				if (json[0].status) {//查找到数据
					var allEquiPara = [];
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
				else {    //不存在查找项
					var txt = "查找失败<br/>";
					txt += "失败原因：";
					txt += "当前局站下不存在满足条件的光纤<br/>";
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
		$("#id").val("");
		$("#name").val("");
	}

});

/**---------------------------修改光纤----------------------------------*/
$(".fiberModify").click(function () {
	var trs = document.querySelectorAll("#fiberTableDetail tbody tr");
	var selectFlag = 0;
	for (var cableTableCount = 0; cableTableCount < trs.length; cableTableCount++) {
		if ($(trs[cableTableCount]).hasClass("currtr")) {
			if (trs[cableTableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
		}
	} if (selectFlag == 0)//未选择
	{
		var txt = "清先选择您要修改的光纤<br/>";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		$(".sidebarDiv").load("html/resource/fiberModify.html");

	}
});
/**GIS相关**/
//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display", "none");
$(".mapDiv").css("display", "block");;

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