/**
 * 
 */
$(function() {
    $(".personnelManage li").click(function () {
        var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        	case"role":
        		 $(".containerDiv").load("html/personnel/role.html");
                 localStorage.setItem("currentUrl","html/personnel/role.html");
        		break;
        	case"user":
        		$(".containerDiv").load("html/personnel/user.html");
            	localStorage.setItem("currentUrl","html/personnel/user.html");
        		break;
        	case"dutyOperator":
        		$(".containerDiv").load("html/personnel/dutyOperatorManage.html");
            	localStorage.setItem("currentUrl","html/personnel/dutyOperatorManage.html");
        		break;
        	case"duty":
        		$(".containerDiv").load("html/personnel/dutySchedule.html");
             	localStorage.setItem("currentUrl","html/personnel/dutySchedule.html");
        		break;
        	default:
        		break;
        }
    });        
});