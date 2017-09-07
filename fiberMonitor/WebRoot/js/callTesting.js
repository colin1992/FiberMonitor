/**
 * 
 */

  /************************实现分页的表格写入函数***************************/
            function setTable(tableData){
                var nums = document.querySelectorAll(".routeTableDetail tbody tr").length;
                var cells = document.getElementById("routeTableDetail").rows.item(0).cells.length;
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
        /**通过rtuId获取光路列表**/   
        function listRoutesByRtuId(rtuId){
               $(".rightTable").css("display","block");
               $(".mapDiv").css("display","none");
         	   var tds = document.querySelectorAll("#routeTableDetail tbody tr td");
         	    for (var i=0;i<tds.length;i++){
         	        tds[i].innerHTML = "";
         	    }//每次点击rtu会先清空右侧表格里面的信息，然后再执行下面的ajax写入右侧的表格。
         	    $.ajax({
                    			type : "post",
                    			async : false,  //异步请求 先执行后续操作，再执行sucess
                    			url : "getRouteByRtuId",
                    			dataType:"json",
                    			data : {
                    				      "id":rtuId, 
                    				    },
                    			success:function(json){
										if(json[0].status){
		               						 var routePara=[]; 
		               	 					 for(var i=1;i<json.length;i++){
		               	 						 var equiPara=[
		               	 						                 json[i].id,
		               	 						                 json[i].name,
		           	    	 						             json[i].rtuName,
		           		   	 						             json[i].rtuOrder,
		           		 						                 json[i].status,
		           		 						                 json[i].isBroken,
		           		 						                 json[i].length,
		           		 						                 json[i].isUplink,
		           		 						                 json[i].description,
		           		 						                 json[i].rtuId,
		           		 						                 json[i].stationAName,
		           		 						                 json[i].createTime,
		           		 						                 json[i].createUser,
		           		 						                 
		           		 						              ];
		               	 							routePara.push(equiPara)
		               	 					}
		               	 					   setTable(routePara);//分页写入表格
		               						
		               				   }
		               			},
                    			error:function(XMLHttpRequest,Error){
                    			
                    		   }
                        });
       
            }
          /**表格范围内禁止系统右键，使用自定义右键**/
            $("#routeTableDetail").hover(function(){
            	 $(document).bind("contextmenu",function(e){   
          		   return false;   
          		 });
            },
            function(){
            	$(document).unbind("contextmenu","")
            });	
            /****************************添加右键菜单功能***********************************/
            $(".routeTableDetail tbody tr").click(function(){
            	$(this).addClass("currtr").siblings().removeClass("currtr");
            	if(this.children[1].innerHTML!=""){
            		$(this).addClass("callTest").siblings().removeClass("callTest");
            	}
                var menu = new BootstrapMenu('.callTest', {
                        actions: [{
                            name: '点名测试',
                            onClick: function() {
                            	$(this).remove();
                            	isPerCallTest();
                            }
                        },
                         {
                            name: '设置优化参数',
                            onClick: function() {
                            	$(this).remove();
                            	$(".sidebarDiv").load("html/resource/pathOptSetting.html");
                           }
                        }]
            	  })
            })
    /**判断是否有点名测试的权限**/
    function isPerCallTest(){
    	 $.ajax({
    		 url:"isPerCallTest",
        	 dataType:'json', //接受数据格式
             async: false,
             data:null,
    		 success: function(json){
    			 if(json[0].status){
    				 $(".sidebarDiv").load("html/curve/callTestingCon.html");//已经选择光路  
    			 }
    			 else{
    				    var txt= "您不具有当前操作的权限，请联系管理员<br/>"
    					var option = {
    								title: "提示",
    								btn: parseInt("0001",2),
    								onOk: function(){
    							    	
    								  }
    							}
    					window.wxc.xcConfirm(txt, "info", option);
    								
    			 }
    		 },	 
    	})
    }    
 /**-------------------开始点名测试按键-----------------------------------*/
       $(".callTestingCon").click(function(){//点击点名测试按键
    	var trs = document.querySelectorAll("#routeTableDetail tbody tr");
    	var callTestChooseFlag=false;
   	    for (var i=0;i<trs.length;i++){
   	        if($(trs[i]).hasClass("currtr")){
   	        	if(trs[i].children[1].innerHTML!=""){//标识不为空，表明一定选中，而且选中的部位空
   	        		callTestChooseFlag=true;
   		    	    i=trs.length;
   		    	 }
   	           
   	        }
   	        
   	    }
    	if(!callTestChooseFlag){//未选择任何光路
    		        var txt= "您尚未选择任何光路，请先选择您要测试的光路<br/>"
					var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){
							    	
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
       	}else{   
       		isPerCallTest();
       	 }
       });

       
      /**---------------------优化参数设置---------------------------------------*/
        $(".setOptimize").click(function () {  //点击设置优化参数
        	var trs = document.querySelectorAll("#routeTableDetail tbody tr");
        	 var selectFlag=0;
      	    for (var tableCount=0;tableCount<trs.length;tableCount++){
      	     if($(trs[tableCount]).hasClass("currtr")){
      	    	 if(trs[tableCount].children[1].innerHTML!="")//标识不为空，表明一定选中，而且选中的部位空
      	    	  selectFlag=1;
      	      }
      	    }if(selectFlag==0){//未选择route
        		var txt= "您尚未选择任何光路，请先选择您要设置参数的光路\n";
 			    var option = {
 						title: "提示",
 						btn: parseInt("0001",2),
 						onOk: function(){//点击确认的执行方法
 						}
 					}
 			  window.wxc.xcConfirm(txt, "info", option);
           	}else{ 
           		$(".sidebarDiv").load("html/resource/pathOptSetting.html");
            }
        });

       /**-------关闭参数设置框-------*/
        $(".sidebar_close").click(function(){
            $(".contentDiv").html("");
            $(".sidebarDiv").html("");
        });
        
        
      
      
 