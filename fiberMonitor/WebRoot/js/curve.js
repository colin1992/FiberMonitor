/**
 * 
 */
$(function() {
    $(".curveManager li").click(function () {
        var id = $(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch(id){
        case"curveQuery":
        	 $(".containerDiv").load("html/curve/curveQuery.html");
             localStorage.setItem("currentUrl","html/curve/curveQuery.html");
        	break;
        case"callTesting":
        	$(".containerDiv").load("html/curve/callTesting.html");
       	    localStorage.setItem("currentUrl","html/curve/callTesting.html");
        	break;
        case"periodTesting":
        	$(".containerDiv").load("html/curve/periodTesting.html");
        	localStorage.setItem("currentUrl","html/curve/periodTesting.html");
        	break;
        case"obstacleTesting":
        	$(".containerDiv").load("html/curve/obstacleTesting.html");
        	localStorage.setItem("currentUrl","html/curve/obstacleTesting.html");
        	break;
        default:
        	break;
        }
    });
      
});

