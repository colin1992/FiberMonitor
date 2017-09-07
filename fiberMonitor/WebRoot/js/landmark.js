/**
 * landmark main js

 */
var landmarkCableId = 0;//全局变量，每次点击光缆数后将ID赋值
var landmarkCableName = "";//全局变量，每次点击光缆数后将Name赋值
/**对特定input加以限定，禁止输入法输入**/
$(function() {  
	
	$('input[name="integer"]').bind({  
        keydown : function() {  
            $(this).val($(this).val().replace(/[^\d]/g, ''));  
        }  
    });  
  
	$('input[name="integer"]').each(function() {  
        var _input = $(this)[0];  
        if (_input.attachEvent) {  
            _input.attachEvent('onbeforepaste', formatPasteDataToInteger);  
        } else {  
            _input.addEventListener('onbeforepaste', formatPasteDataToInteger, false);  
        }  
    });  
      
	$('input[name="float"]').bind({  
		keydown : function() {  
            $(this).val($(this).val().replace(/[^0-9.]/g, ''));  
        }  
    });  
      
	$('input[name="float"]').each(function() {  
        var _input = $(this)[0];  
        if (_input.attachEvent) {  
            _input.attachEvent('onbeforepaste', formatPasteDataToFloat);  
        } else {  
            _input.addEventListener('onbeforepaste', formatPasteDataToFloat, false);  
        }  
    });  
  
    function formatPasteDataToInteger() {  
        clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g, ''));  
    }  
  
    function formatPasteDataToFloat() {  
        clipboardData.setData('text', clipboardData.getData('text').replace(/[^0-9.]/g, ''));  
    }  
  
});  
/*-----------------------形成左侧树------------------*/
$(document).ready(function () {
	var hrefCable = "javascript:cableReload()";
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
						leftTree.add(NodeId, parentId, "地标管理", hrefCable, "", "", "", "", true);
					}
					else if (parseInt(NodeId[0]) == 3) {//为光缆
						for (var index = 2; index < NodeId.length; index++) { //
							equiId += NodeId[index];//拼接字符串
						}
						hrefStaAddress = "javascript:getLandmarkByCableId(" + parseInt(equiId) + ",'" + nodeName + "')";//
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

/*--------------------点击管理节点将会重新载入当前页面-------------------*/
function cableReload() {
	$(".containerDiv").load("html/resource/landmark.html");
}

function getLandmarkByCableId(selectedCableId, selectedCableName) {
	   landmarkCableName = selectedCableName;//存储当前节点的光缆Id和名称
	   landmarkCableId = selectedCableId;
	   $(".rightTable").css("display", "block");
	   $(".mapDiv").css("display", "none"); //隐藏地图
	   $.ajax({
		url: "getLandmarkByCableId",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"cableId": selectedCableId,
		},
		success: function (json) {

			/*------刷新显示地标列表，先清空-------*/
			var tds = document.querySelectorAll("#landmarkTableDetail tbody tr td");
			for (var i = 0; i < tds.length; i++) {
				tds[i].innerHTML = "";
			}

			/*-------------提取当前cable下的landmark-----------------*/
			if (json[0].status) {
				var landmarkPara = [];
				for (var lCount = 1; lCount < json.length; lCount++) {

					var thisLandmark = [];
					/*-------遍历获取每个地标的信息，未完成---------*/
					thisLandmark = [
						json[lCount].id,
						json[lCount].name,
						json[lCount].cableName,
						json[lCount].Longitude,
						json[lCount].Latitude,
						json[lCount].description,
						json[lCount].createTime,
						json[lCount].createUser,
						json[lCount].alterTime,
						json[lCount].alterUser
					];

					landmarkPara.push(thisLandmark);
				}
				setTable(landmarkPara);//分页写入表格
			}
		},
	});
}

/*----------------------查询地标------------------------------*/
$("#doLandmarkSerch").click(function () {
	var landmarkName = $("#searchLandmarkName").val();
	if (landmarkName == "") {    //未输入查询信息
		var txt = "请输入查询条件";
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
		var tds = document.querySelectorAll("#landmarkTableDetail tbody tr td");
		for (var i = 0; i < tds.length; i++) {
			tds[i].innerHTML = "";
		}
		$.ajax({
			url: "searchLandmark",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"landmarkName": landmarkName,
			},
			success: function (json) {
				if (json[0].status) {//查找到数据
					if (json[0].status) {
						var landmarkPara = [];
						for (var lCount = 1; lCount < json.length; lCount++) {

							var thisLandmark = [];
							/*-------遍历获取每个地标的信息，未完成---------*/
							thisLandmark = [
								json[lCount].id,
								json[lCount].name,
								json[lCount].cableName,
								json[lCount].Longitude,
								json[lCount].Latitude,
								json[lCount].description,
								json[lCount].createTime,
								json[lCount].createUser,
								json[lCount].alterTime,
								json[lCount].alterUser
							];

							landmarkPara.push(thisLandmark);//在列表中显示
						}
						setTable(landmarkPara);//分页写入表格
					}

				}
				else {    //不存在查找项
					var txt = "不存在满足条件的条目，请核对后重试";
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
				var txt = "不存在满足条件的条目，请核对后重试";
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
		$("#searchLandmarkName").val("");
	}

});

/************************实现分页的表格写入函数***************************/
function setTable(tableData) {
	var nums = document.querySelectorAll(".landmarkTableDetail tbody tr").length;
	var cells = document.getElementById("landmarkTableDetail").rows.item(0).cells.length;
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
			var trs = document.querySelectorAll("#landmarkTableDetail tbody tr");
			var tds = document.querySelectorAll("#landmarkTableDetail tbody tr td");
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
/***导入地标**/
$(".upload").click(function () {
	$(".sidebarDiv").load("html/resource/uploadLandmark.html");	
});
/**--------------------------新建光缆地标-----------------------------*/
$(".landmarkCreate").click(function () {
	$(".sidebarDiv").load("html/resource/landmarkCreate.html");
});
/**表格范围内禁止系统右键，使用自定义右键**/
$("#landmarkTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能***********************************/
$(".landmarkTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	if (this.children[1].innerHTML != "") {
		$(this).addClass("landmark").siblings().removeClass("landmark");
	}
	else{
		 $(this).removeClass("landmark").siblings().removeClass("landmark");
	}
	var selectId = this.children[1].innerHTML;
	var menu = new BootstrapMenu('.landmark', {
		actions: [{
			name: '修改地标',
			onClick: function () {
				$(this).remove();
				$(".sidebarDiv").load("html/resource/landmarkModify.html");
			}
		},
		{
			name: '删除地标',
			onClick: function () {
				$(this).remove();
				var txt = "警告：删除操作不可恢复！！";
				txt += "请谨慎选择，";
				txt += "确定删除请点击确定键。"
				var option = {
					title: "提示",
					btn: parseInt("0011", 2),
					onOk: function () {//点击确认的执行方法
						delLandmark(selectId);
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}]
	});
})

/**--------------------------修改光缆地标-----------------------------*/
$(".landmarkModify").click(function () {
	var trs = document.querySelectorAll("#landmarkTableDetail tbody tr");
	var selectFlag = 0;
	for (var tableCount = 0; tableCount < trs.length; tableCount++) {
		if ($(trs[tableCount]).hasClass("currtr")) {
			if (trs[tableCount].children[1].innerHTML != "")//标识不为空，表明一定选中，而且选中的部位空
				selectFlag = 1;
		}
	} if (selectFlag == 0)//未选择cable
	{
		var txt = "请先选择您要修改的地标";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		$(".sidebarDiv").load("html/resource/landmarkModify.html");
	}
});
/*--------------------------删除光缆地标-----------------------------*/
$(".landmarkDelete").click(function () {
	var trs = document.querySelectorAll("#landmarkTableDetail tbody tr");
	var selectId = "";
	for (var tableCount = 0; tableCount < trs.length; tableCount++) {
		if ($(trs[tableCount]).hasClass("currtr")) {
			selectId = trs[tableCount].children[1].innerHTML;//标识不为空，表明一定选中，而且选中的部位空
			tableCount = trs.length;
		}
	}
	if (selectId == "")//未选择cable
	{
		var txt = "请先选择您要删除的地标";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法

			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		var txt = "警告：删除操作不可恢复<br/>";
		txt += "请谨慎选择<br/>";
		txt += "确定删除请点击确定键"
		var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				delLandmark(selectId);
			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
});
/**
 * 删除地标
 */
function delLandmark(selectId) {
	$.ajax({
		url: 'landmark/delLandmark',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"landmarkId": selectId
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
					refreashTable(json[0].cableId);//刷新表格
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
	})
}
/***
 * 刷新显示表格
 */
function refreashTable(cableId) {
	$.ajax({
		url: "getLandmarkByCableId",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"cableId": cableId,
		},
		success: function (json) {

			/*------刷新显示地标列表，先清空-------*/
			var tds = document.querySelectorAll("#landmarkTableDetail tbody tr td");
			for (var i = 0; i < tds.length; i++) {
				tds[i].innerHTML = "";
			}

			/*-------------提取当前cable下的landmark-----------------*/
			if (json[0].status) {
				var landmarkPara = [];
				for (var lCount = 1; lCount < json.length; lCount++) {

					var thisLandmark = [];
					/*-------遍历获取每个地标的信息，未完成---------*/
					thisLandmark = [
						json[lCount].id,
						json[lCount].name,
						json[lCount].cableName,
						json[lCount].Longitude,
						json[lCount].Latitude,
						json[lCount].description,
						json[lCount].createTime,
						json[lCount].createUser,
						json[lCount].alterTime,
						json[lCount].alterUser
					];

					landmarkPara.push(thisLandmark);
				}
				setTable(landmarkPara);//分页写入表格
			}
		},
	});
}

/**GIS相关**/
//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display", "none");
$(".mapDiv").css("display", "block");


function openSpeficTree(stationId) {
	leftTree.config.folderLinks = false;
	leftTree.closeAll();
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none"); //隐藏地图
	var stationid = "2_" + stationId;
	leftTree.openTo(stationid, false);
}


