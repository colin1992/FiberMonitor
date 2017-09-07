/**
 * 
 */
$(function() {
    $(".businessSwitch li").click(function () {
        var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        	case "fiberSwitch":
        		$(".containerDiv").load("html/business/routeSwitch.html");
            	localStorage.setItem("currentUrl","html/business/routeSwitch.html");
        	break;
        	case "switchSetting":
        		$(".containerDiv").load("html/business/switchConfigure.html");
            	localStorage.setItem("currentUrl","html/business/switchConfigure.html");
        	break;
        	default:
        	break;
        }
  });  
});