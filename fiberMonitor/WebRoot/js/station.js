/**
 * station main station.js

 */
/**全局变量 当前选中节点区域的名称和ID**/
var thisAreaId=1;
/**点击GIS局站，展开树**/
function openSpeficTree(stationId) {
	tree.config.folderLinks = false;
	tree.closeAll();
	$(".rightTable").css("display", "block");
	$(".mapDiv").css("display", "none");
	var stationid = "2_" + stationId;
	tree.openTo(stationid, true);
}
/**对特定input加以限定，禁止输入法输入**/
$(function() {  
	
	$('input[name="integer"]').bind({  
        keydown : function() {  
            $(this).val($(this).val().replace(/[^\d]/g, ''));  
        }  
    });  
  
	$('input[name="integer"]').each(function() {  
        var _input = $(this)[0];  
        if (_input.attachEvent) {  
            _input.attachEvent('onbeforepaste', formatPasteDataToInteger);  
        } else {  
            _input.addEventListener('onbeforepaste', formatPasteDataToInteger, false);  
        }  
    });  
      
	$('input[name="float"]').bind({  
		keydown : function() {  
            $(this).val($(this).val().replace(/[^0-9.]/g, ''));  
        }  
    });  
      
	$('input[name="float"]').each(function() {  
        var _input = $(this)[0];  
        if (_input.attachEvent) {  
            _input.attachEvent('onbeforepaste', formatPasteDataToFloat);  
        } else {  
            _input.addEventListener('onbeforepaste', formatPasteDataToFloat, false);  
        }  
    });  
  
    function formatPasteDataToInteger() {  
        clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g, ''));  
    }  
  
    function formatPasteDataToFloat() {  
        clipboardData.setData('text', clipboardData.getData('text').replace(/[^0-9.]/g, ''));  
    }  
  
});  
/**--------形成左侧树---------------*/
$(document).ready(function(){
                tree = new dTree('tree');//创建一个对象.
  				tree.config.folderLinks=true;
                tree.config.useCookies=false;
                tree.config.check=true;
                $.ajax({
	                    url:'create_tree',
	                    type:'post', //数据发送方式
	                    dataType:'json', //接受数据格式
	                    async: false,
	                    success: function(json){
	                    	var hrefStation="javascript:stationReload()";
	                    	for(var count=0;count<json.length;count++){
	                    		var areaId=" ";//定义字符串来承接areaId
	                    		 var nodeId=json[count].id;//读取节点id
	                             var parentId=json[count].pid;//读取节点pid
	                             var areaName=json[count].name;
	                             var hrefAddress="";//定义url指向js，当为区域时需要使用
	                             if(parseInt(json[count].id[0])<2){  //只显示区域
	                          	 if(parseInt(json[count].id[0])==1){//为区域
	                          		for(var index=2;index<json[count].id.length;index++){ //读取areaId，    id新式为x_xxx,所以从第二位开始为areaId
	                          			areaId+=json[count].id[index];//拼接字符串，获得字符串形式的areaId
	                          		 }
	                          	   hrefAddress="javascript:getStationByAreaId("+parseInt(areaId)+")";//为区域，添加点击事件，function()入口参数为areaId
	                          	   }
	                            	var nodeName=json[count].name;
	                            	if(nodeId=="0"||nodeId==0){
	                                    tree.add(nodeId,parentId,"局站管理",hrefStation,"","","","",true);
	                                  }else{
	                                  	tree.add(nodeId,parentId,nodeName,hrefAddress,"","","","",false);
	                                  }
	                             	
	                            }
	                    	 }
	                    	 
	                   
	                     },
                });
               document.getElementById("leftTree").innerHTML = tree;
});

/**表格范围内禁止系统右键，使用自定义右键**/
$("#stationTableDetail").hover(function(){
	 $(document).bind("contextmenu",function(e){   
		   return false;   
		 });
},
function(){
	$(document).unbind("contextmenu","")
});	
/****************************添加右键菜单功能***********************************/
$(".stationTableDetail tbody tr").click(function(){
	 $(this).addClass("currtr").siblings().removeClass("currtr");
	 if(this.children[1].innerHTML!=""){
		 $(this).addClass("station").siblings().removeClass("station");
	 }
	 else{
 		 $(this).removeClass("station").siblings().removeClass("station");
 	 }
	var thisAreaName= this.children[4].innerHTML;
   	var thisAreaId= this.children[3].innerHTML;
   	var thisStationId=this.children[1].innerHTML;
	var menu = new BootstrapMenu('.station', {
            actions: [{
                name: '修改局站',
                onClick: function() {
                	$(this).remove();
                	$(".sidebarDiv").load("html/resource/stationModify.html");	
                }
            },
            {
                name: '删除局站',
                onClick: function() {
                	  $(this).remove();
                	  var txt="警告：删除局站将删除局站下的所有资源！！<br/>";
          	    	  txt+="请谨慎操作，";
          	    	  txt+="确认继续请点击确认键。"
           	          var option = {
           						title: "提示",
           						btn: parseInt("0011",2),
           						onOk: function(){//点击确认的执行方法
           							delStation(thisStationId);
           					  }
           					}
           			  window.wxc.xcConfirm(txt, "info", option);
                }
            }]
        });
})


/**-----------------------------点击根节点，重载当前页面-------------------------*/
      function stationReload(){
                $(".containerDiv").load("html/resource/station.html");
      }
    
       /************************实现分页的表格写入函数***************************/
       function setTable(tableData){
           var nums = document.querySelectorAll(".stationTableDetail tbody tr").length;
           var cells = document.getElementById("stationTableDetail").rows.item(0).cells.length;
           var pages = Math.ceil(tableData.length/nums);
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
                   var trs = document.querySelectorAll("#stationTableDetail tbody tr");
                   var tds = document.querySelectorAll("#stationTableDetail tbody tr td");
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
    /**----------------查询局站------------------------*/   
       $(".doSearch").click(function(){
       	  if($("#searchStationName").val()=="")//未输入查询条件
    	   {
	       		var txt = "请输入查询条件";
	    		var option = {
	    			title: "提示",
	    			btn: parseInt("0001", 2),
	    			onOk: function () {//点击确认的执行方法
	
	    			}
	    		}
	    		window.wxc.xcConfirm(txt, "info", option);
    	   }
       	  else{
       		   /*每次查询都会先清空一下表格内容*/
	       	    var tds = document.querySelectorAll("#stationTableDetail tbody tr td");
	       	    for (var i=0;i<tds.length;i++){
	       	        tds[i].innerHTML = "";
	       	    }
		       	 $.ajax({
		  	       	 url:"searchStation",//服务器地址
		  	       	 type:"POST",//采用POST请求
		  	       	 dataType:"json",//回传数据类型
		  	       	 timeout:2000,
		  	       	 data:{
		  	       		    'stationName':$("#searchStationName").val(),
		  	             	'areaId':thisAreaId,
		  	          },
		  	        success:function(data)
		  	       	  {
		  	        	  $(".contentDiv").html("");
	                      $(".sidebarDiv").html(""); 
		  	        	 if(data[0].status){
		  	        		 var stationPara=[];
		  	        		 for(var count=1;count<data.length;count++){
		  	        			var searchStationPara=[];
					            searchStationPara=[ 
					            		              data[count].stationId,
					            				      data[count].stationName,
					            				      data[count].areaId,
					            				      data[count].areaName,
					            				      data[count].stationLongitude,
					            				      data[count].stationLatitude,
					            				      data[count].stationDescription,
					            				      data[count].stationCreteTime,
					            				      data[count].stationCreteUser,
					            				      data[count].stationAlterTime,
					            				      data[count].stationAlterUser
					            				   ];
					            		 
					            stationPara.push(searchStationPara);
					       }
			   	   		   setTable(stationPara);//实现分页的表格写入函数
			            }
				   	   	else{
					   	   	    var tds = document.querySelectorAll("#stationTableDetail tbody tr td");
			            	    for (var i=0;i<tds.length;i++){
			            	         tds[i].innerHTML = "";
			            	      }
			            	      var txt="查找失败，当前不存在满足条件的条目<br/>";
			          	    	  var option = {
			           						title: "提示",
			           						btn: parseInt("0001",2),
			           						onOk: function(){//点击确认的执行方法
			           					}
			           			   }
			           			  window.wxc.xcConfirm(txt, "info", option);    
				   	   	     }
		                  },
		                  error:function(XMLHttpRequest,Error,F,data)
		        	      {
		                	  var txt="查找失败，当前不存在满足条件的条目<br/>";
		          	    	  var option = {
		           						title: "提示",
		           						btn: parseInt("0001",2),
		           						onOk: function(){//点击确认的执行方法
		           					}
		           			   }
		           			  window.wxc.xcConfirm(txt, "info", option);   
		        	    }
		  	       	});
       	} 
       	  
       	/**--------成功与否都将搜索框清空-------*/
       	 $("#searchStationName").val("");
           
       }); 
       /**---------------------增加局站---------------------*/
       $(".stationAdd").click(function(){
           $(".sidebarDiv").load("html/resource/stationAdd.html");
       });

       /**---------------------修改局站---------------------*/
       $(".stationModify").click(function () {
       	
       	var trs = document.querySelectorAll("#stationTableDetail tbody tr");
      	    var selectFlag="";
      	    for (var i=0;i<trs.length;i++){
      	    if($(trs[i]).hasClass("currtr")){
      	    		selectFlag=trs[i].children[1].innerHTML;
	      	    	i=trs.length;
      	      }
      	    }
      	    if(selectFlag=="")//未选择局站
      		   {
      	    	     var txt="请先选择您要修改的局站"
        	         var option = {
        						title: "提示",
        						btn: parseInt("0001",2),
        						onOk: function(){//点击确认的执行方法
        					  }
        					}
        			  window.wxc.xcConfirm(txt, "info", option);
      		   }
      	     else{
   		   	    $(".sidebarDiv").load("html/resource/stationModify.html");	
      		   }
       	
          
       });
  /*---------------------删除局站--------------------*/
       $(".stationDelete").click(function () {
       	
       	var trs = document.querySelectorAll("#stationTableDetail tbody tr");
      	    var selectId="";
      	    var delAreaName;
      	    var delAreaId;
      	    for (var count=0;count<trs.length;count++){
      	    if($(trs[count]).hasClass("currtr")){
      	    	delAreaName= trs[count].children[4].innerHTML;
      	    	delAreaId= trs[count].children[3].innerHTML;
      	    	selectId=trs[count].children[1].innerHTML;
      	    	count=trs.length;
      	       }
      	    }
      	    if(selectId=="")//未选择局站
      		   {
      	    	     var txt="请先选择您要删除的局站"
        	         var option = {
        						title: "提示",
        						btn: parseInt("0001",2),
        						onOk: function(){//点击确认的执行方法
        					  }
        					}
        			  window.wxc.xcConfirm(txt, "info", option);
      		   }
      	     else{
      	    	  var txt="警告：删除局站将删除局站下的所有资源！！<br/>";
      	    	  txt+="请谨慎操作<br/>";
      	    	  txt+="确认继续请点击确认键"
       	          var option = {
       						title: "提示",
       						btn: parseInt("0011",2),
       						onOk: function(){//点击确认的执行方法
       							delStation(selectId);
       					  }
       					}
       			  window.wxc.xcConfirm(txt, "info", option);
   		    	
      		   }
      });
    function delStation(delSatId){
    	 $.ajax({
   	       	 url:"station/delStation",//服务器地址
   	       	 type:"POST",//采用POST请求
   	       	 dataType:"json",//
   	       	 timeout:5000,
   	       	 data:{
   	             	'stationId':delSatId,
   	             	
   	       	  },
   	   	success: function(json){
   	        $(".sidebarDiv").html("");
	    	var txt="";
	    	if(json[0].status){
	    		txt+="删除成功<br/>"
	    	}
	    	else{
	    		txt+="删除失败，失败原因："+json[0].err;
	    	}
		     var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
							getStationByAreaId(thisAreaId);
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
	      },
	     error:function(XMLHttpRequest,Error,F,data)
	      {
	    	    $(".sidebarDiv").html("");
	    	    var txt="删除失败<br/>";
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
    
    /**
     * areaId展示该区域下的局站
     */
             function getStationByAreaId(areaId){
            	    $(".rightTable").css("display", "block");
            		$(".mapDiv").css("display", "none");
                      thisAreaId=areaId;
                	 /*每次点击一下叶节点都会先清空一下表格内容*/
                 	   var tds = document.querySelectorAll("#stationTableDetail tbody tr td");
                 	    for (var i=0;i<tds.length;i++){
                 	        tds[i].innerHTML = "";
                 	  }
                	  $.ajax({
                           url:"getStation",
                           type:'post', //数据发送方式
                           dataType:'json', //接受数据格式
                           timeout:2000,
                           data:{
                        	      'areaId':areaId,
                        	     },
                           async: false,
                           success: function(data){
                           var stationPara=[];
                           /*-------------提取各区域下的局站，进行显示渲染-----------------*/
                           	for(var stationCount=0;stationCount<data.length;stationCount++){
                           		var para=[ 
                           		              data[stationCount].stationId,
                           				      data[stationCount].stationName,
                           				      data[stationCount].areaId,
                           				      data[stationCount].areaName,
                           				      data[stationCount].stationLongitude,
                           				      data[stationCount].stationLatitude,
                           				      data[stationCount].stationDescription,
                           				      data[stationCount].stationCreteTime,
                           				      data[stationCount].stationCreteUser,
                           				      data[stationCount].stationAlterTime,
                           				      data[stationCount].stationAlterUser
                           				      ];
                           		
                           		stationPara.push(para);
                           	}
                           	setTable(stationPara);//分页写入表格
                          },
                           error:function(XMLHttpRequest,Error,F)
             	       	  {
                           	
             	       	       alert("访问出错了！！"+"\n请求状态："+XMLHttpRequest.status+"\n错误原因："+Error);
             	       	
             	       	 }
                       });
                }   