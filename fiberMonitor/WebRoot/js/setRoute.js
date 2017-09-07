/**
 * 
 */
/**-----------全局变量，所有的区域及其局站id-----------------*/
var allAreaName=[];
var allAreaId=[];
var allStationName=[];
var allStationId=[];
var ACount=0;
var ZCount=0;
var selectStationId=0;
/**----------------页面载入后查询数据库，获取区域和相关局站信息------------------------------*/
$(document).ready(function(){
	$.ajax({
        url:'create_tree',
        type:'post', //数据发送方式
        dataType:'json', //接受数据格式
        async: false,
        success: function(json){
        	for(var count=0;count<json.length;count++){
        		var stationId=" ";//定义字符串来承接areaId
        		 var nodeId=json[count].id;//读取节点id
        		 var areaId="";
        		 var parentId=json[count].pid;//读取节点pid
                 var nodeName=json[count].name;
                 if(parseInt(json[count].id[0])<3){  //取到局站
	              	 if(parseInt(json[count].id[0])==1){//为区域
	              		for(var index=2;index<json[count].id.length;index++){ //读取areaId
	              			areaId+=json[count].id[index];//拼接字符串，获得字符串形式的areaId
	              		}
	              		allAreaId.push(parseInt(areaId));
	              		allAreaName.push(nodeName);
	              		
	              	 }//找出区域
	            }
        	}
        	
        	/**-------------遍历循环，读取每个区域的局站信息--------------*/
        	var areaStationId=new Array(allAreaId.length);
        	var areaStationName=new Array(allAreaId.length);
        	for(var maxd=0;maxd<areaStationId.length;maxd++){
        		areaStationId[maxd]=[];
        		areaStationName[maxd]=[];
        	}
        	for(var count=0;count<json.length;count++){
        	var stationId=" ";//定义字符串来承接areaId
       		 var nodeId=json[count].id;//读取节点id
       		 var parentId=json[count].pid;//读取节点pid
             var nodeName=json[count].name;
             if(parentId!="0"){
              		if(parseInt(nodeId[0])==2)//为局站
	              		 {
	              		   
	              	       for(var pidCount=2;pidCount<nodeId.length;pidCount++){
	              	    	 stationId+=nodeId[pidCount];
	              	      }
	              	       for(var icount=0;icount<allAreaId.length;icount++){
	              	    	   if(parseInt(parentId[2])==allAreaId[icount])
	              	    		   {
	              	    		     areaStationId[icount].push(stationId);
	              	    		     areaStationName[icount].push(nodeName);
	              	    		   }
	              	       }
	              	      
	              	    }
	            }
             }
        	  allStationName=areaStationName;//二维数组的局站名
        	  allStationId=areaStationId;//二维数组的局站id
        	  for(var areaIndex=0;areaIndex<allAreaName.length;areaIndex++){
          	    $("#AAreaName").append("<option value='"+allAreaId[areaIndex]+"'>"+allAreaName[areaIndex]+"</option>");//给区域选项菜单赋值
          	    $("#ZAreaName").append("<option value='"+allAreaId[areaIndex]+"'>"+allAreaName[areaIndex]+"</option>");//给区域选项菜单赋值
          	 }
        },
        error:function(XMLHttpRequest,Error,F,json)
     	  {
        	
     	
     	 }
    });
});
/**---------------跨段checkBox改变--------------*/
    $('#isCross').change(function(){
    	if($("#isCross").is(':checked')){
    		$("#passAreaName").empty();
     	    $("#passAreaName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
     	    for(var areaIndex=0;areaIndex<allAreaName.length;areaIndex++){
         	    $("#passAreaName").append("<option value='"+allAreaId[areaIndex]+"'>"+allAreaName[areaIndex]+"</option>");//给区域选项菜单赋值
     	    }
     	    $("#passStationName").empty();
     	    $("#passStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//
    		$("#passAreaName").removeAttr("disabled");  
    		$("#passStationName").removeAttr("disabled"); 
    	}
    	else{//不跨段
    	    $("#passAreaName").empty();
    	    $("#passAreaName").append("<option value='"+"index"+"'>"+"--不跨段--"+"</option>");//给区域选项菜单赋值
    	    $("#passStationName").empty();
    	    $("#passStationName").append("<option value='"+"index"+"'>"+"--不跨段--"+"</option>");//
   		    $("#passAreaName").attr("disabled","disabled");
    		$("#passStationName").attr("disabled","disabled");
    	}
      // alert("isCross:"+$("#isCross").is(':checked'));
    })

	/*------------------------A端区域下拉框状态改变-----------------------------------*/
	  $('#AAreaName').change(function(){
		  $("#AAreaName option[value='index']").remove(); 
		  $("#AStationName").empty();
		 ACount= parseInt($(this).children('option:selected').val()); 
  	    $("#AStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
        for(var areaIndex=0;areaIndex<allStationName[ACount-1].length;areaIndex++){
     	    $("#AStationName").append("<option value="+allStationId[ACount-1][areaIndex]+">"+allStationName[ACount-1][areaIndex]+"</option>");//给区域选项菜单赋值
     	   
     	}
	  });
	  
	  /**------------------------区域下拉框状态改变-----------------------------------*/
	  $('#passAreaName').change(function(){
	    $("#passAreaName option[value='index']").remove(); 
		$("#passStationName").empty();
		ACount= parseInt($(this).children('option:selected').val()); 
  	    $("#passStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
        for(var areaIndex=0;areaIndex<allStationName[ACount-1].length;areaIndex++){
     	    $("#passStationName").append("<option value="+allStationId[ACount-1][areaIndex]+">"+allStationName[ACount-1][areaIndex]+"</option>");//给区域选项菜单赋值
     	   
     	}
	  });
	  
	  /**------------------------Z端区域下拉框状态改变-----------------------------------*/
	  $('#ZAreaName').change(function(){
		  $("#ZStationName").empty();
		  $("#ZAreaName option[value='index']").remove(); 
  		 ZCount= parseInt($(this).children('option:selected').val()); 
  		$("#ZStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
  		for(var areaIndex=0;areaIndex<allStationName[ZCount-1].length;areaIndex++){
      	    $("#ZStationName").append("<option value="+allStationId[ZCount-1][areaIndex]+">"+allStationName[ZCount-1][areaIndex]+"</option>");//给区域选项菜单赋值
      	   
      	}
  	  });
	  
	/**----------------A局站改变---------------*/
	 $('#AStationName').change(function(){
		    $("#AStationName option[value='index']").remove();
		
		 }) 
		 
	/**----------------局站改变---------------*/
	 $('#passStationName').change(function(){
		    $("#passStationName option[value='index']").remove();
		
		 }) 	 
	/*----------------Z局站改变---------------*/
	 $('#ZStationName').change(function(){
	    $("#ZStationName option[value='index']").remove();
	
	 })
	/*---------------------------------close按键----------------------------------------------*/
	 $(".sidebar_close").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 /*-------------------------------------------cancel---------------------------*/
	 $(".cancel").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 /*---------------------------------确定按键----------------------------------------------*/
	 $(".setConfirm").click(function(){
		 /*-----------跨段-------------*/
		 if($("#isCross").is(':checked')){
			 var IDs=[
			            parseInt($("#AStationName").val()),
			            parseInt($("#passStationName").val()),
			            parseInt($("#ZStationName").val())
			         ];
		 }
		 /*-------------不跨段-------------*/
		 else{
			 var IDs=[
			            parseInt($("#AStationName").val()),
			            parseInt($("#ZStationName").val())
			         ];
		 }
		 
		 var IDStr=JSON.stringify(IDs);
		 $.ajax({
			    url:'setRoute',
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		        	    "id":IDStr,
		             },
		         success: function(json){
		        	var crossStation=[];
		        	var route=[];
		        	var topological=[];
		        	var preparatoryId=[];
		        	var framePortOrder=[];
		        	if(json[0].status){
		        		for(var count=1;count<json.length;count++){
		        			route.push(json[count].rtuName+"-"+json[count].rtuPortOrder+"——"+json[count].frameName+"-"+json[count].framePortOrder);
		        			var top=[json[count].topologicalRoute,json[count].topologicalPoint];
		        			topological.push(top);//拓扑图+
		        			preparatoryId.push(json[count].preparatoryId);//preparatoryId
		        			framePortOrder.push(json[count].framePortOrder);//framePortOrder
		        		}
		        		crossStation.push(json[1].topologicalStation);//局站id
		        		localStorage.setItem('route', JSON.stringify(route));
		        		localStorage.setItem('topological', JSON.stringify(topological));
		        		localStorage.setItem('preparatoryId', JSON.stringify(preparatoryId));
		        		localStorage.setItem('framePortOrder', JSON.stringify(framePortOrder));
		        		localStorage.setItem('crossStation', JSON.stringify(crossStation));
		        		var routeSetedPara=[];
		        		localStorage.setItem("routeSetedPara",JSON.stringify(routeSetedPara));
		        		$(".sidebarDiv").load("html/resource/selectRoute.html");
		   	            $(".contentDiv").html("");
		        	 }
		        	else{
		        		  $(".sidebarDiv").load("html/resource/setRouteFail.html");
		   	              $(".contentDiv").html("");
		        	}
		         },
		        error:function(){
		        	  $(".sidebarDiv").load("html/resource/setRouteFail.html");
	   	              $(".contentDiv").html("");
		        },  
			 
			 
		 });  
        
     });
	 /************************实现分页的表格写入函数***************************/
	 function showTable(tabledata){    //传入数据为二位数组
	     var tableData = tabledata;
	     var nums = document.querySelectorAll(".routeTableDetail tbody tr").length;
	     var cells = document.getElementById("routeTableDetail").rows.item(0).cells.length;
	     var pages = Math.ceil(tableData.length/nums);
	    // alert("nums:"+nums+"\ncells:"+cells+"\npages:"+pages+"\ndataLength:"+tableData.length);
	     var thisData = function (curr) {
	         var tableDataCurr= [];
	         var last = curr*nums - 1;
	         last = last >= tableData.length ? (tableData.length - 1) : last;
	         tableDataCurr = tableData.slice(curr*nums - nums,last + 1);
	         return tableDataCurr;
	     };

	     laypage({
	         cont: 'pageon',
	         pages: pages,
	         jump: function(obj) {
	             var currTableData = thisData(obj.curr);
	             var trs = document.querySelectorAll("#routeTableDetail tbody tr");
	             var tds = document.querySelectorAll("#routeTableDetail tbody tr td");
	             for (var i=0;i<tds.length;i++){
	                 tds[i].innerHTML = "";
	             }
	             for (var i = 0; i < currTableData.length; i++) {
	                 var Ele = trs[i].children;
	                 for(var j=0; j<cells-1; j++){
	                     Ele[0].innerHTML = i + 1;
	                     Ele[j+1].innerHTML = currTableData[i][j];
	                 }
	             }
	         }
	     });
	 }
