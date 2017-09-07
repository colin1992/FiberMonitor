/**
 * 
 */
$(function(){
    $(".degradationAnalyze li").click(function(){
    	var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        	case"lightPower":
        		 $(".containerDiv").load("html/degradation/lightPower.html"); 
                 localStorage.setItem("currentUrl","html/degradation/lightPower.html");
        	break;
        	case"degradAnalyze":
        		$(".containerDiv").load("html/degradation/degradationAnalyse.html");
            	localStorage.setItem("currentUrl","html/degradation/degradationAnalyse.html");
            break;
        	case"parameterCon":
        		$(".containerDiv").load("html/degradation/parameterCon.html");
                localStorage.setItem("currentUrl","html/degradation/parameterCon.html");
            break;
        	default:
        	break;
        }
    });
 
});