/**主控设置js*/
/**全局变量 当前选中的RTU ID 点击左侧树后赋值**/
var rtuId = 0;
var serverStamp=0;//服务器当前时间的时间戳
/**生成RTU树**/
tree = new dTree('tree');//创建一个对象.
tree.config.folderLinks = true;
tree.config.useCookies = false;
tree.config.check = true;
$.ajax({
	url: 'masterConTree',
	type: 'post', //数据发送方式
	dataType: 'json', //接受数据格式
	async: false,
	success: function (json) {
		$(json).each(function () {
			for (var count = 0; count < json.length; count++) {
				var deviceId = "";//
				var nodeId = json[count].id;//读取节点id
				var parentId = json[count].pid;//读取节点pid             
				var hrefAddress = "";//
				if (parseInt(json[count].id[0]) < 4) {//
					if (parseInt(json[count].id[0]) == 3) {//
						for (var index = 2; index < json[count].id.length; index++) {
							deviceId += json[count].id[index];
						}
						hrefAddress = "javascript:setCurrentRtu(" + deviceId + ")";
					}
					if (parseInt(json[count].id[0]) == 0) {//为根节点
						deviceId = "0";
						hrefAddress = "javascript:reload()";//
						json[count].name = "主控设置";
					}
					var nodeName = json[count].name;
					tree.add(nodeId, parentId, nodeName, hrefAddress, "", "", "", "", false);
				}
			}
		});
	}
});
document.getElementById("leftTree").innerHTML = tree;

/**重载当前页面**/
function reload() {
	$(".containerDiv").load("html/system/controlConfig.html");
}
/**获取RTU类型，不同的RTU对应不同的管理页面**/
function getRtuType(rtuId){
	var type=0;//0为普通RTU  1为备纤光源RTU
	$.ajax({
		url: "masterCon/getRtuType",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				type=json[0].type;
			}
		},
		error: function (XMLHttpRequest, Error) {
		},
	});
	return type;
}
        
/**获取当前RTU的告警方式**/
function getAlarmWay() {
	$.ajax({
		url: "getAlarmWay",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				$("#currentAlarmWay").val(json[0].alarmWay);
			}
		},
		error: function (XMLHttpRequest, Error) {
		},
	});
}
/**获取所有的告警方式**/
function getAllAlarmWay() {
	$.ajax({
		url: "getAllAlarmWay",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				$("#alarmWay").empty();//将下拉框清空
				var alarmWays = json[0].alarmWay;
				for (var i = 0; i < alarmWays.length; i++) {
					$("#alarmWay").append("<option value=" + alarmWays[i].id + " >" + alarmWays[i].name + "</option>");
				}
			}
		},
		error: function (XMLHttpRequest, Error) {
		},
	});
}
/**更新RTU告警方式**/
$("#setAlarmWay").click(function () {
	var alarmId = $("#alarmWay").val();
	if (alarmId == 6 || $("#alarmWay").find("option:selected").text() == "无告警提示") {
		var txt = "告警方式设置为\"无告警提示\"后将不会有任何告警提示."
		txt += "确认继续？"
		var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				setAlarmWay(alarmId);
			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	else {
		setAlarmWay(alarmId);
	}
})
function setAlarmWay(alarmId) {
	$.ajax({
		url: "setAlarmAlert",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"CM": rtuId,
			"alarmId": alarmId,
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {//
				txt = "成功更新RTU告警方式.";
			}
			else {
				txt = "更新告警方式失败，失败原因："+json[0].err;
			}
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
					getAlarmWay();
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
		},
		error: function (XMLHttpRequest, Error) {
			var txt = "更新告警方式失败.";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限.";
			}
			else {
				txt += "网络错误.";
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
/**设置当前选中RTU **/
function setCurrentRtu(deviceId) {
	$(".mapDiv").css("display", "none");
	rtuId = deviceId;
	var rtuType=getRtuType(deviceId);
	if(rtuType==0){//普通RTU
		//获取服务器时间
		getServceTime();
		$("#mainRtu").css("display","block");
		$("#switchRtu").css("display","none");
		var info=[];
		setMoudleTable(info);
		getAlarmWay();
		getAllAlarmWay();
		setTimeout("setCurrentTime()", 1000);//1000为1秒钟。  
	}
	else{//切换RTU
		$("#mainRtu").css("display","none");
		$("#switchRtu").css("display","block");
	}	
}
/**获取服务器时间**/
function getServceTime(){
	$.ajax({
		url: "getServerTime",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
		},
		success: function (json) {
			if (json[0].status) {
				serverStamp=json[0].stamp;
			}
			else{/**获取服务器时间信息失败
			       利用客户端的时间，有风险
			     **/
				serverStamp=new Date().getTime();
			}
		},
		error: function (XMLHttpRequest, Error) {
			/**获取服务器时间信息失败
		       利用客户端的时间，有风险
		     **/
			serverStamp=new Date().getTime();
		},
	});
	//转换显示时间信息,此处serverStamp必须为数字
	var currentTime=formatStampDate(new Date(Number(serverStamp)));
	$("#currentTime").val(currentTime);
}

/**将时间戳转化为时间
 * RTU时间同步利用服务器的时间戳
 * 为了在界面显示时间信息，先从后台获取后台的时间信息，然后以改时间为基准进行计时显示
 * **/
function formatStampDate(now) {
	  var year=now.getFullYear();
	  var month=now.getMonth()+1;
	  month=month<10?("0"+month):month;
	  var date=now.getDate();
	  date=date<10?("0"+date):date;
	  var hour=now.getHours();
	  hour=hour<10?("0"+hour):hour;
	  var minute=now.getMinutes();
	  minute=minute<10?("0"+minute):minute;
	  var second=now.getSeconds();
	  second=second<10?("0"+second):second;
	  return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
	}       
/**获取RTU时间信息**/
$("#getRtuTime").click(function () {
	$.ajax({
		url: "masterCon/getRtuTime",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				$("#rtuTime").val(json[0].T8);
			}
			else {
				var txt = "获取RTU时间信息失败<br/>";
				txt += "失败原因：连接超时" + json[0].err;
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
			var txt = "获取RTU时间信息失败<br/>";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限";
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
})
/**刷新RTU时间信息**/
$("#setRtuTime").click(function () {
	$.ajax({
		url: "masterCon/setRtuTime",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
			"time": $("#currentTime").val(),
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {//
				txt = "成功刷新RTU时间";
			}
			else {
				txt = "刷新RTU时间失败<br/>";
				txt += "失败原因：连接超时，无响应";
			}
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
		},
		error: function (XMLHttpRequest, Error) {
			var txt = "刷新RTU时间失败<br/>";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限";
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
})
/**获取RTU网络信息**/
$("#getRtuIp").click(function () {
	$.ajax({
		url: "masterCon/getRtuIp",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				$("#submask").val(json[0].netMask);
				$("#gateWay").val(json[0].gateway);
				$("#ipv4").val(json[0].IP);
			}
			else {
				var txt = "获取RTU网络信息失败<br/>";
				txt += "失败原因：" + json[0].err;
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
			var txt = "获取RTU网络信息失败<br/>";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限";
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
})
/**设置RTU网络信息**/
$("#setRtuIp").click(function () {
	if (validateForm()) {
		$.ajax({
			url: "masterCon/setRtuIp",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"rtuId": rtuId,
				"submask": $("#submask").val(),
				"gateWay": $("#gateWay").val(),
				"ipv4": $("#ipv4").val(),
			},
			success: function (json) {
				var txt = "";
				if (json[0].status) {//
					txt = "成功刷新RTU网络地址";
				}
				else {
					txt = "刷新RTU网络地址失败<br/>";
					txt += "失败原因："+json[0].err;
				}
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			},
			error: function (XMLHttpRequest, Error) {
				var txt = "刷新RTU网络地址失败<br/>";
				txt += "失败原因：";
				if (XMLHttpRequest.status == 401) {
					txt += "您不具有当前操作的权限";
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
})
/**获取切换RTU网络信息**/
$("#getSwitchRtuIp").click(function () {
	$.ajax({
		url: "masterCon/getRtuIp",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//查找到数据
				$("#switchSubmask").val(json[0].netMask);
				$("#switchGateWay").val(json[0].gateway);
				$("#switchIpv4").val(json[0].IP);
			}
			else {
				var txt = "获取RTU时间信息失败<br/>";
				txt += "失败原因：连接超时" + json[0].err;
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
			var txt = "获取RTU时间信息失败<br/>";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限";
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
})
/**设置切换RTU网络信息**/
$("#setSwitchRtuIp").click(function () {
	if (validateForm()) {
		$.ajax({
			url: "masterCon/setRtuIp",
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"rtuId": rtuId,
				"submask": $("#switchSubmask").val(),
				"gateWay": $("#switchGateWay").val(),
				"ipv4": $("#switchIpv4").val(),
			},
			success: function (json) {
				var txt = "";
				if (json[0].status) {//
					txt = "成功刷新RTU网络地址";
				}
				else {
					txt = "刷新RTU网络地址失败<br/>";
					txt += "失败原因："+json[0].err;
				}
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
					}
				}
				window.wxc.xcConfirm(txt, "info", option);
			},
			error: function (XMLHttpRequest, Error) {
				var txt = "刷新RTU网络地址失败<br/>";
				txt += "失败原因：";
				if (XMLHttpRequest.status == 401) {
					txt += "您不具有当前操作的权限";
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
})
/**重启RTU**/
$("#restartSwitchRtu").click(function(){
	var txt="一般只在RTU出现故障时才进行重启操作，请谨慎选择。RTU重启过程可能需要花费几分钟的时间，在此期间不能进行测试操作。"
	var option = {
			title: "提示",
			btn: parseInt("0011", 2),
			onOk: function () {//点击确认的执行方法
				restartRtu();
			}
		}
		window.wxc.xcConfirm(txt, "info", option);
})
$("#restartRtu").click(function(){
	    var txt="一般只在RTU出现故障时才进行重启操作，请谨慎选择。RTU重启过程可能需要花费几分钟的时间，在此期间不能进行测试操作。"
		var option = {
				title: "提示",
				btn: parseInt("0011", 2),
				onOk: function () {//点击确认的执行方法
					restartRtu();
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
})
function restartRtu(){
	$.ajax({
		url: "masterCon/restart",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			var txt = "";
			if (json[0].status) {//
				txt = "成功下发重启指令，RTU重启中，可能需要花费几分钟的时间。";
			}
			else {
				txt = "下发重启指令失败,";
				txt += "失败原因："+json[0].err;
			}
			var option = {
				title: "提示",
				btn: parseInt("0001", 2),
				onOk: function () {//点击确认的执行方法
				}
			}
			window.wxc.xcConfirm(txt, "info", option);
		},
		error: function (XMLHttpRequest, Error) {
			var txt = "下发重启指令失败，";
			txt += "失败原因：";
			if (XMLHttpRequest.status == 401) {
				txt += "您不具有当前操作的权限";
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
/**普通RTU表单*/
function validateForm() {
	return $(".netForm").validate({
		rules: {
			submask: {
				required: true,
				ipv4: true
			},
			gateWay: {
				required: true,
				ipv4: true
			},
			ipv4: {
				required: true,
				ipv4: true
			}
		},
		messages: {
			submask: {
				required: "请输入子网掩码",
				ipv4: "请输入正确的子网掩码"
			},
			gateWay: {
				required: "请输入网关地址",
				ipv4: "请输入正确的网关地址"
			},
			ipv4: {
				required: "请输入RTU地址",
				ipv4: "请输入正确的IPv4地址"
			}
		}
	}).form();
}
/**切换RTU表单*/
function validateSwitchForm() {
	return $(".switchNetForm").validate({
		rules: {
			submask: {
				required: true,
				ipv4: true
			},
			gateWay: {
				required: true,
				ipv4: true
			},
			ipv4: {
				required: true,
				ipv4: true
			}
		},
		messages: {
			submask: {
				required: "请输入子网掩码",
				ipv4: "请输入正确的子网掩码"
			},
			gateWay: {
				required: "请输入网关地址",
				ipv4: "请输入正确的网关地址"
			},
			ipv4: {
				required: "请输入RTU地址",
				ipv4: "请输入正确的IPv4地址"
			}
		}
	}).form();
}
/**获取模块信息**/
$("#getMoudleInfo").click(function(){
	$.ajax({
		url: "masterCon/getMoudleInfo",
		dataType: 'json', //接受数据格式
		async: false,
		data: {
			"rtuId": rtuId,
		},
		success: function (json) {
			if (json[0].status) {//
				var moudles=json[0].moudles;//获取各模块信息
				var Info=[];
				for(var i=0;i<moudles.length;i++){
					var moudle=[
					              moudles[i].order,
					              moudles[i].type,
					              moudles[i].portNum,
					              moudles[i].status,
					              moudles[i].useNum,
					              moudles[i].setTime,
					           ];
					Info.push(moudle);
				}
				setMoudleTable(Info);
				
			}
		}
	})
})
/**写入表格**/
function setMoudleTable(moudles){
	 var trs = document.querySelectorAll("#moudleTable tbody tr");
     var tds = document.querySelectorAll("#moudleTable tbody tr td");
     var cells = document.getElementById("moudleTable").rows.item(0).cells.length;
     for (var i=0;i<tds.length;i++){//先清空
         tds[i].innerHTML = "";
     }
     for (var i = 0; i < moudles.length; i++) {
         var Ele = trs[i].children;
         for(var j=0; j<cells; j++){
            Ele[j].innerHTML=moudles[i][j];
         }
     }
}
/**设置时间表**/
function setCurrentTime() {
	/**
	 * 每隔一秒执行一次，时间加一秒，体现在时间戳为加1000
	 * 为防止将时间戳视为字符串处理，相加时强制将其置为Number类型**/
	serverStamp=Number(serverStamp)+1000;
	var currentTime=formatStampDate(new Date(serverStamp));
	$("#currentTime").val(currentTime);
	setTimeout("setCurrentTime()", 1000);//1000为1秒钟。 
}

/**GIS相关**/
$("#controlMapArea").css("display", "block");//初始化时显示地图
function openSpeficTree(stationId) {
	$(".rightArea").css("display", "block");
	$(".mapDiv").css("display", "none");
	tree.config.folderLinks = false;
	tree.closeAll();
	var stationid = "2_" + stationId;
	tree.openTo(stationid, false);
}     