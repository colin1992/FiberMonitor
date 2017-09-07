/**
 * 在载入增加cable对话框时执行
 * 查询数据库，查找所有区域
 */
/*-----------全局变量，所有的区域及其局站id-----------------*/
var allAreaName=[];
var allAreaId=[];
var allStationName=[];
var allStationId=[];
var ACount=0;
var ZCount=0;
var selectStationId=0;
/*----------------页面载入后查询数据库，获取区域和相关局站信息------------------------------*/
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
        	
        	/*-------------遍历循环，读取每个区域的局站信息--------------*/
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
    });
});
	/**------------------------A端区域下拉框状态改变-----------------------------------*/
	  $('#AAreaName').change(function(){
		$("#AStationName").empty();
		$("#AAreaName option[value='index']").remove();
		ACount= parseInt($('option:selected', '#AAreaName').index()); //当前选区域的index
  	    $("#AStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
        for(var areaIndex=0;areaIndex<allStationName[ACount].length;areaIndex++){
     	    $("#AStationName").append("<option value="+allStationId[ACount][areaIndex]+">"+allStationName[ACount][areaIndex]+"</option>");//给区域选项菜单赋值
     	   
     	}
	  });
	  /**------------------------Z端区域下拉框状态改变-----------------------------------*/
	  $('#ZAreaName').change(function(){
		  $("#ZStationName").empty();
		  $("#ZAreaName option[value='index']").remove();
  		 ZCount= parseInt( $('option:selected', '#ZAreaName').index()); 
  		$("#ZStationName").append("<option value='"+"index"+"'>"+"请选择"+"</option>");//给区域选项菜单赋值
  		for(var areaIndex=0;areaIndex<allStationName[ZCount].length;areaIndex++){
      	    $("#ZStationName").append("<option value="+allStationId[ZCount][areaIndex]+">"+allStationName[ZCount][areaIndex]+"</option>");//给区域选项菜单赋值
      	   
      	}
  	  });
	/**选择局站后取出index选项**/
	  $('#ZStationName').change(function(){
		 $("#ZStationName option[value='index']").remove();
  	  });
	  $('#AStationName').change(function(){
			 $("#AStationName option[value='index']").remove();
	  	  });
	/**---------------------------------close按键----------------------------------------------*/
	 $(".sidebar_close").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 /**-------------------------------------------cancel---------------------------*/
	 $(".cancel").click(function(){
         $(".contentDiv").html("");
         $(".sidebarDiv").html("");
     });
	 /**-------------验证表单-------------------**/
	  function validateForm() {
		    return $(".cableForm").validate({
		        rules: {
		        	    cableLength: {
			                required: true,
			                number: true
		               },
		               ARemainLen: {
			                required: true,
			                number: true
	                  },
	                   ZRemainLen: {
			                required: true,
			                number: true
	                  },
		               cableCoreCount: {
			                required: true,
			                number: true
		              },
		              reIndex: {
			                required: true,
			                range: [1, 2]
		              }
		        },
		        messages: {
		        	cableLength: {
		                 required: "请输入光缆长度",
		                 number: "请输入数字"
		            },
		            ARemainLen: {
		                required: "请输入A端盘留长度",
		                number: "请输入数字"
		            },
		            ZRemainLen: {
		                required: "请输入Z端盘留长度",
		                number: "请输入数字"
		            },
		            cableCoreCount: {
		                required: "请输入Z端盘留长度",
		                number: "请输入数字"
		            },
		            reIndex: {
		                required: "请输入光纤折射率",
		                range: "请输入1~2的数字"
		            },
		        }
		    }).form();
		}
	 /**---------------------------------确定按键----------------------------------------------*/
	 $(".confirm").click(function(){
		 selectStationId=parseInt($("#AStationName").val());
		if(validateForm()){
			$.ajax({
			    url:'cable/addCable',
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		        	   "cableName":$("#cableName").val(),
		        	   "cableLength":$("#cableLength").val(),
		        	   "AStationId":$("#AStationName").val(),
		        	   "AStationName":$("#AStationName").find("option:selected").text(),
		        	   "ARemainLen":$("#ARemainLen").val(),
		        	   "ZStationId":$("#ZStationName").val(),
		        	   "ZStationName":$("#ZStationName").find("option:selected").text(),
		        	   "ZRemainLen":$("#ZRemainLen").val(),
		        	   "newworkLevel":$("#newworkLevel").val(),
		        	   "laidWay":$("#laidWay").val(),
		        	   "cableCoreCount":$("#cableCoreCount").val(),
		        	   "cableCore":$("#cableCore").val(),
		        	   "refractiveIndex":$("#refractiveIndex").val(),
		        	   "description":$("#description").val(),
		             },
		        success: function(json){
		  	       	        $(".sidebarDiv").html("");
		  			    	var txt="";
		  			    	if(json[0].status){
		  			    		txt+="增加成功<br/>"
		  			    	}
		  			    	else{
		  			    		txt+="增加失败，请重试";
		  			    	}
		  				     var option = {
		  								title: "提示",
		  								btn: parseInt("0001",2),
		  								onOk: function(){//点击确认的执行方法
		  									refreashTable(selectStationId);
		  								}
		  							}
		  					  window.wxc.xcConfirm(txt, "info", option);
		  			      },
		  			    error:function(XMLHttpRequest,Error)
		  			      {
		  			    	    $(".sidebarDiv").html("");
		  			    	    var txt="增加失败<br/>";
		  			    	    txt+="失败原因：";
		  			    	    if(XMLHttpRequest.status==401){
		  			    	        txt+="您不具有当前操作的权限<br/>";
		  			    	     }
		  			    	     else{
		  			    	        	txt+="网络错误<br/>";
		  			    	        	txt+="状态码："+XMLHttpRequest.status;
		  			    	        }
		  			    	      var option={
		  			    	   					title: "提示",
		  			    	   					btn: parseInt("0001",2),
		  			    	   					onOk: function(){//点击确认的执行方法
		  			    	   						
		  			    	  			       }
		  			    	   				}
		  			    	   		  window.wxc.xcConfirm(txt, "info", option);
		  			  }
		 });  
		} 
	 });
 function refreashTable(selectStationId){
	 var tds = document.querySelectorAll("#cableTableDetail tbody tr td");
	    for (var i=0;i<tds.length;i++){
	        tds[i].innerHTML = "";
	    }
	    $.ajax({
		    	 url:"getCablesByStationId",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "stationId":selectStationId,           	 
	                  },
				 success: function(json){
					 if(json[0].status)
					  {
						 for(var jsonCount=1;jsonCount<json.length;jsonCount++){
							var cableData=[
								              jsonCount,
								              json[jsonCount].id,
								              json[jsonCount].name,
								              json[jsonCount].Length,
								              json[jsonCount].aPortName,
								              json[jsonCount].aRemainLength,
								              json[jsonCount].zPortName,
								              json[jsonCount].zRemainLength,
								              json[jsonCount].networkLevel,
								              json[jsonCount].laidWay,
								              json[jsonCount].coreStrct,
								              json[jsonCount].coreNumber,
								              json[jsonCount].refractiveIndex,
								              json[jsonCount].description,
								              json[jsonCount].createTime,
								              json[jsonCount].createUser,
//								              json[jsonCount].alterTime,
//								              json[jsonCount].alterUser
								              
							               ];
				        	 
							SearchStationTable(cableData);
		
				         }
				     }
				 }
		});  
	    
   }
    function SearchStationTable(tableData) {      
 	 //  alert("table");
   	   var trs = document.querySelectorAll("#cableTableDetail tbody tr");
   	    for(var i=0; i<trs.length; i++){
   	        var Ele = trs[i].children;
   	        if(!Ele[0].innerHTML){
   	        	Ele[0]=i;
   	            for (var j=0;j<tableData.length; j++){
   	                Ele[j].innerHTML =tableData[j];
   	                i = trs.length;
   	            }
   	        }
   	    }
   	}

