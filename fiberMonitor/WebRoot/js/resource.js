/**
 * 
 */

$(function(){
    /*子导航中下拉菜单点击 页面跳转*/
    
    /****线路资源管理****/
    $(".lineContent li").click(function(){
    	
        var id =$(this).attr("id");
        $(".navContentDiv").hide();
        $(".containerDiv").show();
        switch (id){
          case "cable":  //cable
        	  $(".containerDiv").load("html/resource/cable.html");
              localStorage.setItem("currentUrl","html/resource/cable.html");
           break;
          case "route":  //route
        	  $(".containerDiv").load("html/resource/path.html");
              localStorage.setItem("currentUrl","html/resource/path.html");
           break;
          case "fiber":  //fiber
        	  $(".containerDiv").load("html/resource/fiber.html");
              localStorage.setItem("currentUrl","html/resource/fiber.html");
           break;
          case "jumper":  //jumper
        	  $(".containerDiv").load("html/resource/jumper.html");
          	localStorage.setItem("currentUrl","html/resource/jumper.html");
           break;
          case "port":  //port
        	  $(".containerDiv").load("html/resource/port.html");
              localStorage.setItem("currentUrl","html/resource/port.html");
              break;
          default:
        	   break;
        }
    });


   /****设备资源管理****/
    $(".equipmentContent li").click(function(){
    	var id =$(this).attr("id");;
    	$(".navContentDiv").hide();
        $(".containerDiv").show();
        switch (id){
        case "cabinet":  //cabinet
        	 $(".containerDiv").load("html/resource/cabinet.html");
             localStorage.setItem("currentUrl","html/resource/cabinet.html");
         break;
        case "frame":  //frame
        	 $(".containerDiv").load("html/resource/wiringframe.html");
             localStorage.setItem("currentUrl","html/resource/wiringframe.html");
         break;
        case "rtu":  //rtu
        	$(".containerDiv").load("html/resource/RTU.html");
            localStorage.setItem("currentUrl","html/resource/RTU.html");
         break;
        case "rack":  //rack
        	 $(".containerDiv").load("html/resource/rack.html");
             localStorage.setItem("currentUrl","html/resource/rack.html");
         break;
        default:
      	   break;
      }
    });
    /****地图资源管理****/
    $(".mapContent li").click(function(){
    	var id = $(this).attr("id");
        $(".containerDiv").show();
        $(".navContentDiv").hide();
        switch(id){
        	case"area":
        		$(".containerDiv").load("html/resource/area.html");
                localStorage.setItem("currentUrl","html/resource/area.html");
        		break;
        	case"station":
        		 $(".containerDiv").load("html/resource/station.html");
                 localStorage.setItem("currentUrl","html/resource/station.html");
        		break;
        	case"landmark":
        		 $(".containerDiv").load("html/resource/landmark.html");
                 localStorage.setItem("currentUrl","html/resource/landmark.html");
        		break;
        }
    });
});