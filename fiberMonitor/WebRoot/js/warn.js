/**
 * 
 */
$(function(){
    $(".warnManage li").click(function(){
        var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        	case"warnQuery":
        		$(".containerDiv").load("html/warn/warnQuery.html");
                localStorage.setItem("currentUrl","html/warn/warnQuery.html");
        	break;
        	case"warnBank":
        		 $(".containerDiv").load("html/warn/warnBank.html");
                 localStorage.setItem("currentUrl","html/warn/warnBank.html");
            	break;
        	case"warnSetting":
        		$(".containerDiv").load("html/warn/warnSetting.html");
                localStorage.setItem("currentUrl","html/warn/warnSetting.html");
            break;
            default:
            break;
        }
//        if(ind == 0){
//            $(".containerDiv").load("html/warn/warnQuery.html");
//            localStorage.setItem("currentUrl","html/warn/warnQuery.html");
//        }
//        else if(ind == 1){
//            $(".containerDiv").load("html/warn/warnBank.html");
//            localStorage.setItem("currentUrl","html/warn/warnBank.html");
//        }
//        else{
//            $(".containerDiv").load("html/warn/warnSetting.html");
//            localStorage.setItem("currentUrl","html/warn/warnSetting.html");
//        }
    });


    /*告警查询*/

    /*告警经验库
    $(".checkWarn").click(function(){
        $(".bgDiv").show();
        $(".checkWarnContent").show();
    });

    $(".modifyWarn").click(function(){
        $(".bgDiv").show();
        $(".modifyWarnContent").show();
    });

    $(".createWarn").click(function(){
        $(".bgDiv").show();
        $(".createWarnContent").show();
    });*/



})