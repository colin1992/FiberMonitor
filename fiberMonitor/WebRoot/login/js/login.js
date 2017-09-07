
$(document).ready(function(){
	//设置验证码
	$.idcode.setCode();
	$('div.poem-stanza').addClass('highlight');	
	/***
	找回密码
	**/
	$("#getPassword").click(function() {
		$("#loadPasswordDiv").load("login/html/getPassword.html"); 
		$("#loadPassword").css({"display":"block"});
	});	
	//登录
	$("#submitLogin").click(function(){
		if(validateForm()){
			doLogin();
		}
	})
});

function doLogin(){
	$.ajax({
		cache: false,
		dataType:'json',
		type: "POST",
		url:"dologin",
		data: {
			"userName":$("#user").val(),
			"password":Encrypt($("#password").val())
		},
		async: false,
		success: function(data) {
			if(data[0].status){//登录成功
				localStorage.clear();
				window.location.replace("index.html");
			}else{//登录失败
				$(".msg").text(data[0].msg);
			}
		}
	});
}
/***AES加密***/
function Encrypt(word){
    var key = CryptoJS.enc.Utf8.parse("fiberMonitorSyst");

    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}
/***AES解密***/
function Decrypt(word){
    var key = CryptoJS.enc.Utf8.parse("fiberMonitorSyst");

    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}
function validateForm(){
	if($("#user").val()==""){
		$(".msg").text("用户名不能为空");
		return false;
	}
	else if($("#password").val()==""){
		$(".msg").text("密码不能为空");
		return false;
	}
	else if($("#valid").val()==""){
		$(".msg").text("验证码不能为空");
		return false;
	}
	else if(!$.idcode.validateCode()){
		$(".msg").text("验证码错误。");
		return false;
	}
	return true;
}
