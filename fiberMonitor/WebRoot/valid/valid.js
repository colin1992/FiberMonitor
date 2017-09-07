/**
 * 系统验证
 */
//页面载入后
 $(document).ready(function(){
	 $.ajax({
		 url: 'valid/getServerMac',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
			},
			success: function (json) {
				$("#serverMac").val(json[0].serverIp);
			},
			error: function (XMLHttpRequest, Error) {
				
			}
	 })
 })

 /**提交按键**/
  function doValidate() {
	 //读取验证码，全局替换-
	 var serialCode=$("#code").val().replace(/-/g, "");
	 $.ajax({
		 url: 'valid/validate',
			type: 'post', //数据发送方式
			dataType: 'json', //接受数据格式
			async: false,
			data: {
				"serialCode":serialCode,
			},
			success: function (json) {
				 var txt="";
			     if(json[0].status){
			    	 txt="恭喜，您已成功完成注册。现在请点击\"确定\"键后开始使用系统,系统初始登录账号为admin，密码123456，请登录后及时修改密码！";
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
			     else{
			    	 txt="您输入的注册码不正确，请核对后重新输入。如果您尚未获得注册码，请联系经销商获取注册码，感谢您选择本系统。";
			    	 var option = {
				    			title: "提示",
				    			btn: parseInt("0001", 2),
				    			onOk: function () {//点击确认的执行方法
									
								},
						}
				    	window.wxc.xcConfirm(txt, "info", option);
			     }
			},
			error: function (XMLHttpRequest, Error) {
				 var txt="网络故障，请稍后重试。";
		    	 var option = {
			    			title: "提示",
			    			btn: parseInt("0001", 2),
			    			onOk: function () {//点击确认的执行方法
								
							},
					}
			    	window.wxc.xcConfirm(txt, "info", option);
			}
			
	 })
 }