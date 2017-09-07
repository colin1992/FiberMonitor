/**
 * 
 */
$(function () {
    /****日志管理内容****/
    $(".logContent li").click(function () {
        var id =$(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        	case"sms":
        		 $(".containerDiv").load("html/system/noteLog.html");
                 localStorage.setItem("currentUrl", "html/system/noteLog.html");
        		break;
        	case"handle":
        		 $(".containerDiv").load("html/system/handleLog.html");
                 localStorage.setItem("currentUrl", "html/system/handleLog.html");
        		break;
        	case"resource":
        		 $(".containerDiv").load("html/system/resourceLog.html");
                 localStorage.setItem("currentUrl", "html/system/resourceLog.html");
        		break;
        	case"business":
        		 $(".containerDiv").load("html/system/businessLog.html");
                 localStorage.setItem("currentUrl", "html/system/businessLog.html");
        		break;
        	case"rtu":
        		 $(".containerDiv").load("html/system/RTULog.html");
                 localStorage.setItem("currentUrl", "html/system/RTULog.html");
        		break;
        	default:
        		break;
        }
       

    });
    /****设备管理内容****/
    $(".configContent li").click(function () {
        var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
	        case"centerControl":
	        	 $(".containerDiv").load("html/system/controlConfig.html");
	             localStorage.setItem("currentUrl", "html/system/controlConfig.html");
	        	break;
	        case"emailSetting":
	        	 $(".containerDiv").load("html/system/emailConfig.html");
	             localStorage.setItem("currentUrl", "html/system/emailConfig.html");
	        	break;
	        case"smsSetting":
	        	 $(".containerDiv").load("html/system/noteConfig.html");
	             localStorage.setItem("currentUrl", "html/system/noteConfig.html");
	        	break;
	        default:
	        	break;
        }
    });
    /****关闭短信邮件设置*****/
    $(".email_note_Config_close").click(function () {
        $(".containerDiv").html("");
        $(".navContentDiv:eq(" + 5 + ")").show();
    });
})
