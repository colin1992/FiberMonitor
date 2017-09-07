/**全局变量 当前要进行参数配置的保护组ID**/
var groupId=0;
/**------------------生成左侧树----------------------*/
$(document).ready(function () {
	var hrefReload = "javascript:Reload()";
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
			for (var JsonCount = 0; JsonCount < json.length; JsonCount++) {
				var equiId = "";//定义字符串来承接
				var NodeId = json[JsonCount].id;//读取节点id
				var nodeName = json[JsonCount].name;
				var parentId = json[JsonCount].pid;//读取节点pid
				if (parseInt(NodeId[0]) < 4) {  //显示到RTU
					if (NodeId == "0" || NodeId == 0) {
						leftTree.add(NodeId, parentId, "切换配置", hrefReload, "", "", "", "", true);
					}
					else if (parseInt(NodeId[0]) == 3) {//为RTU
						for (var index = 2; index < NodeId.length; index++) { //
							equiId += NodeId[index];//拼接字符串
						}
						hrefStaAddress = "javascript:getRouteMatchByRtuId(" + parseInt(equiId) + ")";//
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
			
		}
	});

});

//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display", "none");
$(".mapDiv").css("display", "block");

$("#backToGis").click(function () {
	$(".rightTable").css("display", "none");
	$(".mapDiv").css("display", "block");
})


/**--------------------点击光路管理节点，重载当前页面---------------------------*/
function Reload() {
	$(".containerDiv").load("html/business/switchConfigure.html");
}
/**-------------------点击RTU节点，获取当前RTU的配对信息------------------------*/
var ifClickRtu = false;
//全局变量，当前选中的RTU
var currentRtuId = 0;
var routeList = [];
function getRouteMatchByRtuId(rtuId) {
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	currentRtuId = rtuId;
	ifClickRtu = true;
	var trs = document.querySelectorAll("#switchConfigTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	}//先清空
	$.ajax({
		url: "getAllRouteMatchByRtuId",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId
		},
		success: function (json) {
			if (json[0].status) {

				var tds = document.querySelectorAll("#switchConfigTableDetail tbody tr td");
				for (var i = 0; i < tds.length; i++) {
					tds[i].innerHTML = "";
				}
				for (var jsonCount = 1; jsonCount < json.length; jsonCount++) {
					var equiPara = [jsonCount];
					for (var key in json[jsonCount]) {//遍历json对象的每个key/value对,p为key
						equiPara.push(json[jsonCount][key]);
					}
					setTable(equiPara);
				}
			}
		},
	});
}

function setTable(tableData) {
	var trs = document.querySelectorAll("#switchConfigTableDetail tbody tr");
	for (var i = 0; i < trs.length; i++) {
		var Ele = trs[i].children;
		if (!Ele[0].innerHTML) {
			Ele[0] = i;
			for (var j = 0; j < tableData.length; j++) {
				Ele[j].innerHTML = tableData[j];
				i = trs.length;
			}
		}
	}
}
/**=====点击参数设置按键======**/
$("#setGroupObstaclePara").click(function(){
	var trs = document.querySelectorAll("#switchConfigTableDetail tbody tr");
    var groupId="";
    for (var i=0;i<trs.length;i++){
        if($(trs[i]).hasClass("currtr")){
        	groupId=trs[i].children[1].innerHTML;
           i=trs.length;
        }	        
    }
    if(groupId==""){
    	var txt="您尚未选择配对组，请先选择您要设置保护参数的配对组.";
    	var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
					}
				}
	   window.wxc.xcConfirm(txt, "info", option);
    }
    else{
    	setGroupObstaclePara(groupId);
    }
})
/**======障碍告警参数设置的function=======**
 *  
 */
function setGroupObstaclePara(id){
	groupId=id;//给全全局变量赋值
	$(".sidebarDiv").load("html/business/groupTestPara.html");
}
/**表格范围内禁止系统右键，使用自定义右键**/
$("#switchConfigTableDetail").hover(function () {
	$(document).bind("contextmenu", function (e) {
		return false;
	});
},
	function () {
		$(document).unbind("contextmenu", "")
	});
/****************************添加右键菜单功能(进行单条删除)***********************************/
$(".switchConfigTableDetail tbody tr").click(function () {
	$(this).addClass("currtr").siblings().removeClass("currtr");
	var thisMatchId = this.children[1].innerHTML;
	if (thisMatchId != "") {
		$(this).addClass("group").siblings().removeClass("group");
	}
	else{
		$(this).removeClass("group").siblings().removeClass("group");
	}
	var menu = new BootstrapMenu('.group', {
		actions: [
			{
				name: "保护参数设置",
				onClick: function () {
					$(this).remove();
					setGroupObstaclePara(thisMatchId);
					
				}
			},
		    {
				name: '删除保护组',
				onClick: function () {
					$(this).remove();
					var matchId = [];
					matchId.push(thisMatchId);
					var txt = "删除保护组将取消配对光路的保护状态，确认删除请点击\"确认\"键。";
					var option = {
						title: "提示",
						btn: parseInt("0011", 2),
						onOk: function () {//点击确认的执行方法
								delSwitchGroup(matchId);
						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
		  }]
	});
})

function startMatch() {
	if (!ifClickRtu) {
		var txt = "请先进入点击选择一个RTU<br/>";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法
			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else
		$(".sidebarDiv").load("html/business/routeMatch.html");
}
/***删除保护组***/
function delSwitchGroup(matchId){
	$.ajax({
		type: "post",
		async: false,  //异步请求 先执行后续操作，再执行sucess
		url: "switch/delSwitchGroup",
		dataType: "json",
		data: {
			"selectMatchId": JSON.stringify(matchId),
			"rtuId": currentRtuId
		},
		success: function (data) {
			if (data[0].status) {
				$(".sidebarDiv").html("");
				getRouteMatchByRtuId(currentRtuId);
				var txt = "您已成功删除当前保护组。";
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
			else {
				var txt = "删除失败,失败原因：" + data[0].err;
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

			var txt = "删除失败,失败原因：";
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
function deleteMatch() {
	if (ifClickRtu == false)
		alert("请先选择rtu");
	else
		$(".sidebarDiv").load("html/business/deleteMatch.html");
}

function openSpeficTree(stationId) {
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	leftTree.config.folderLinks = false;
	leftTree.closeAll();
	var stationid = "2_" + stationId;
	leftTree.openTo(stationid, false);
}
