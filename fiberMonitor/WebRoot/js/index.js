var moveTimer = null;
var isRight = true;
var isDown = true;
/*-------页面载入后加载，用于建立与服务器的socket连接----------*/
$(document).ready(function () {
	setAccountInfo();//填充用户名
	isSuperAdmin();//检查是否有用户管理权限
	if(localStorage.getItem('alarmInfo')==null){//用于存储当前告警消息的item  避免刷新后丢失
		localStorage.setItem('alarmInfo',$("#notificationDiv").html());
		localStorage.setItem('alarmCount',$("#notifyNum").text());
	}
	$("#notifyNum").text(localStorage.getItem('alarmCount'));
	$("#notificationDiv").html(localStorage.getItem('alarmInfo'));
	//给查看按键绑定事件
	$('a[class="checkInfo"]').each(function(index,item){//给localStorage中的告警条目绑定展开事件
		   $(this).live("click",function(){
			    $(this).parent().css('display','none');
				$(this).parent().next().css('display','block');
		})
	})
	//给收起按键绑定事件
	$('a[class="hideInfo"]').each(function(index,item){//给localStorage中的告警条目绑定隐藏事件
		   $(this).live("click",function(){
			    $(this).parent().css('display','none');
				$(this).parent().prev().css('display','block');
		})
	})
	//给删除按键绑定事件
	$('a[class="delInfo"]').each(function(index,item){//给localStorage中的告警条目绑定删除事件
		   $(this).live("click",function(){
			   var notifyNum=parseInt($("#notifyNum").text());
				notifyNum--;
				localStorage.setItem('alarmCount',notifyNum);//更新存储的告警数目
				$("#notifyNum").text(notifyNum);//告警统计减1
				if(notifyNum==0){
					$(alarm).html('<span>当前无系统消息</span>');
				}
				//移除前为了避免留白，去掉边距
				$(this).parent().parent().css({"border":"0px",'margin-bottom':'0px'});
				//移除当前项
				$(this).parent().parent().remove();
				localStorage.setItem('alarmInfo',$("#notificationDiv").html());//更新存储的告警信息
		})
	})
	//给清除按键绑定事件
	$('a[class="clearInfo"]').each(function(index,item){//给localStorage中的告警条目绑定隐藏事件
		   $(this).live("click",function(){
			    localStorage.setItem('alarmCount',0);//更新存储的告警数目
				$("#notifyNum").text(0);//告警统计0
				$("#notificationDiv").html('<span>当前无系统消息</span>');
				localStorage.setItem('alarmInfo',$("#notificationDiv").html());//更新存储的告警信息
		})
	})
	if ('WebSocket' in window) {
		var url = document.location.href.split("index")[0] + "webSocket";//通过读取浏览器地址栏来获取构建webSocket的地址
		url = "ws" + url.split("http")[1];
		websocket = new WebSocket(url);
	}
	else {
		var txt = "您的浏览器不支持webSocket，为了更好的用户体验，建议您升级或更换浏览器\n";
		var option = {
			title: "提示",
			btn: parseInt("0001", 2),
			onOk: function () {//点击确认的执行方法
			}
		}
		window.wxc.xcConfirm(txt, "info", option);
	}
	websocket.onopen = function () {
	};
	/**-------产生告警信息----------*/
	websocket.onmessage = function (event) {
		var notifyNum=parseInt($("#notifyNum").text());
		if(notifyNum==0){
			$("#notificationDiv").html("");
		}
		notifyNum++;
		$("#notifyNum").text(notifyNum);
		localStorage.setItem('alarmCount',notifyNum);//更新存储的告警条目
		log(event.data);
	};
	websocket.onerror = function (evnt) {
		websocket = null;
	};
	   /**--------断开连接-------*/
	websocket.onclose = function (event) {
		websocket = null;
	}

})
/**------显示告警信息----------
 * message的格式为 title+"context:"+context
 * 
 * */
function log(message) {
	var alarm = document.getElementById('notificationDiv');
	/**一条消息的div**/
	var infoDiv=document.createElement('div');
	var id=parseInt($("#notifyNum").text());
	$(infoDiv).attr("id",id);//当前消息Id
	$(infoDiv).css("border","1px solid #AAAAAA");//当前消息Id
	$(infoDiv).css('wordWrap','break-word');
	$(infoDiv).css('margin-bottom','5px');
	var infoTitle=message.split("context:")[0];
	/**消息标题div**/
	var titleDiv=document.createElement('div');
	$(titleDiv).append("<span style='color:red'>"+infoTitle+"</span>");
	/**显示详情链接**/
	var detialA=document.createElement('a');
	$(detialA).attr('href',"javascript:void(0)");
	$(detialA).text("查看详情");
	$(detialA).attr('class',"checkInfo");
	$(titleDiv).append(detialA);
	$(detialA).live('click',function(){
		$(this).parent().css('display','none');
		$(this).parent().next().css('display','block');
		
	})
	/**收起**/
	var hideA=document.createElement('a');
	$(hideA).attr('href',"javascript:void(0)");
	$(hideA).css({"margin-left":"4px","color":"#4c28eb"});
	$(hideA).text("收起");
	$(hideA).attr('class',"hideInfo");
	$(hideA).live('click',function(){
		$(this).parent().css('display','none');
		$(this).parent().prev().css('display','block');
		
	})
	/**告警详情div*/
	var detailDiv=document.createElement('div');
	$(detailDiv).append('<span>'+message.split("context:")[1].replace(new RegExp("\n","gm"),"<br/>")+'</span>');//告警详情
	$(detailDiv).append(hideA);
	$(detailDiv).css('display','none');
	/**删除**/
	var delA=document.createElement('a');
	$(delA).css({"margin-left":"4px","color":"#4c28eb"});
	$(delA).attr('href',"javascript:void(0)");
	$(delA).attr('class',"delInfo");
	$(delA).text("删除");
	$(delA).live('click',function(){
		var notifyNum=parseInt($("#notifyNum").text());
		notifyNum--;
		localStorage.setItem('alarmCount',notifyNum);//更新存储的告警数目
		$("#notifyNum").text(notifyNum);//告警统计减1
		if(notifyNum==0){
			$(alarm).html('<span>当前无系统消息</span>');
		}
		//移除前先去掉边距
		$(this).parent().parent().css({"border":"0px",'margin-bottom':'0px'});
		//移除当前项
		$(this).parent().parent().remove();
		localStorage.setItem('alarmInfo',$("#notificationDiv").html());//更新存储的告警信息
	})
	$(detailDiv).append(delA);
	/**清除***/
	var clearA=document.createElement('a');
	$(clearA).css({"margin-left":"4px","color":"#4c28eb"})
	$(clearA).attr('href',"javascript:void(0)");
	$(clearA).attr('class',"clearInfo");
	$(clearA).text("清除");
	$(clearA).live('click',function(){
		localStorage.setItem('alarmCount',0);//更新存储的告警数目
		$("#notifyNum").text(0);//告警统计0
		$(alarm).html('<span>当前无系统消息</span>');
	}) 
	$(detailDiv).append(clearA);
	$(infoDiv).append(titleDiv);//添加标题div
	$(infoDiv).append(detailDiv);//添加详情div 
	$(alarm).prepend(infoDiv);
	localStorage.setItem('alarmInfo',$(alarm).html());//更新存储的告警信息
}

/**
 * 获取当前登录用户的用户名**/
function setAccountInfo() {
	$.ajax({
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		url: 'getAccountInfo',
		data: '',
		success: function (json) {
			if (json[0].status) {
				$(".userAccount").text(json[0].account);
				$(".username").text(json[0].account);
				$(".email").text(json[0].email);
				$(".phone").text(json[0].phone);
			}
		}
	})
}
/**
 * 注销账号**/
function logout() {
	$.ajax({
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		url: 'logout',
		data: '',
		success: function (json) {
			if (json[0].status) {
				localStorage.clear();
				window.location.href = "Login.html";
			}
		}
	})
}

/**检查session index.js*/
function checkSession() {
	$.ajax({
		url: 'checkSession',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {
			if (!json[0].status) {
				var txt = "由于长时间未使用，当前登录信息已过期，请重新登录";
				var option = {
					title: "提示",
					btn: parseInt("0001", 2),
					onOk: function () {//点击确认的执行方法
						localStorage.clear();
						window.location.href = "Login.html";
					},
					onClose: function () {
						localStorage.clear();
						window.location.href = "Login.html";
					},
				}
				window.wxc.xcConfirm(txt, "info", option);
			}
		}
	})
}
$(function () {
	/*点击navBtns li，展现下方navContent的相应内容*/
	$(".navBtns li").click(function () {
		checkSession();
		try {
			if (timeTicket === undefined) {
				// console.log("变量未定义.");
			} else {
				clearInterval(timeTicket)
				timeTicket = null;
				//window.console && console.log("变量已定义.");
			}
		} catch (e) {
			// va 未声明
			// window.console && console.log("变量未声明，");
		}
		var ind = $(this).index();
		localStorage.setItem("navIndex", ind);  //FIXME
		localStorage.setItem("currentUrl", "");
		if ($(this).hasClass("curr")) {
			$(".containerDiv").html("");
			$(".sidebarDiv").html("");
			$(".navContentDiv:eq(" + ind + ")").show();
		}
		if (!$(this).hasClass("curr")) {
			$(".containerDiv").html("");
			$(".sidebarDiv").html("");
			$(".navContentDiv:visible").hide();
			$(".navContentDiv:eq(" + ind + ")").show();
			var currli = $(this).siblings(".curr");
			if (currli.size() > 0) {
				currli.removeClass("curr");
			}
			$(this).addClass("curr");
		}
	});
});
function isSuperAdmin() {
	$.ajax({
		type: "post",
		async: false,  //异步请求 先执行后续操作，再执行sucess
		url: "isPermittedRole",
		dataType: "json",
		data: null,
		timeout: 2000,
		success: function (json) {
			if (json[0].status) {
				$(".roleManage").show();
				$(".userManage").show();
			}
		},
	})
}
//浏览器兼容 取得浏览器可视区高度   
function getWindowInnerHeight() {
	var winHeight = window.innerHeight
		|| (document.documentElement && document.documentElement.clientHeight)
		|| (document.body && document.body.clientHeight);
	return winHeight;
}

// 浏览器兼容 取得浏览器可视区宽度   
function getWindowInnerWidth() {
	var winWidth = window.innerWidth
		|| (document.documentElement && document.documentElement.clientWidth)
		|| (document.body && document.body.clientWidth);
	return winWidth;
}

/**  
 * 显示遮罩层  
 */
function showOverlay() {
	// 遮罩层宽高分别为页面内容的宽高   
	$('.overlay').css({ 'height': getWindowInnerHeight(), 'width': getWindowInnerWidth() });
	$('.overlay').show();
} 

/***
 * 用于将光端口转换为数字  通过形如Mi-Xj的端口序号获取1-64的数字编号**/
 function getPortOrder(order){
	var portRow=0;
	switch(order[3]){
		case"A":
			portRow=1;
		break;
		case"B":
			portRow=2;
		break;
		case"C":
			portRow=3;
		break;
		case"D":
			portRow=4;
		break;
			default:
		break;
	}
	var ind=parseInt(order[4]);
	return parseInt(order[1]-1)*8+(ind-1)*4+portRow;
}