/**
 * 
 */
/**------------------生成左侧树----------------------*/
  $(document).ready(function(){
	  var hrefReload="javascript:Reload()";
	  leftTree = new dTree('leftTree');//创建一个对象.
	  leftTree.config.folderLinks=true;
	  leftTree.config.useCookies=false;
	  leftTree.config.check=true;
	  $.ajax({
	      url:'create_tree',
	      type:'post', //数据发送方式
	      dataType:'json', //接受数据格式
	      async: false,
	      success: function(json){
	          for(var JsonCount=0;JsonCount<json.length;JsonCount++){
              	var equiId="";//定义字符串来承接
                var NodeId=json[JsonCount].id;//读取节点id
              	var nodeName=json[JsonCount].name;
                  var parentId=json[JsonCount].pid;//读取节点pid
                  if(parseInt(NodeId[0])<4){  //显示到RTU
                      if(NodeId=="0"||NodeId==0){
                        	leftTree.add(NodeId,parentId,"光路管理",hrefReload,"","","","",true);
                       }
                      else if(parseInt(NodeId[0])==3){//为RTU
                          	 for(var index=2;index<NodeId.length;index++){ //
                          		equiId+=NodeId[index];//拼接字符串
                          	 }
                          	hrefStaAddress="javascript:getRouteByRtuId("+parseInt(equiId)+")";//
                        	leftTree.add(NodeId,parentId,nodeName,hrefStaAddress,"","","","",false);
                       }
                     else{
                      		leftTree.add(NodeId,parentId,nodeName,"","","","","",false);
                      }
                  }
             
              }
              document.getElementById("leftTree").innerHTML = leftTree;  
           },
             
          });
	  
  });
  /**--------------------点击光路管理节点，重载当前页面---------------------------*/
    function Reload(){
    	$(".containerDiv").load("html/resource/path.html");
    }
    
    var currentRtuId=0;
  /**-------------------点击RTU节点，获取当前RTU的光路------------------------*/
    function getRouteByRtuId(rtuId){
    	var cle=[];
    	setTable(cle);
    	//先清空
    	    $.ajax({
    		    	 url:"getRouteByRtuId",
    		    	 dataType:'json', //接受数据格式
    	             async: false,
    	             data:{
    	                     "id":rtuId,           	 
    	                  },
    				 success: function(json){
    					 if(json[0].status){
    						 var tds = document.querySelectorAll("#routeTableDetail tbody tr td");
    					 	 for (var i=0;i<tds.length;i++){
    					 	        tds[i].innerHTML = "";
    					 	  }
    					 	 var routePara=[]; 
    	 					 for(var i=1;i<json.length;i++){
    	 						 var equiPara=[
    	 						                 json[i].id,
    	 						                 json[i].name,
	    	 						             json[i].stationAName,
	   	 						                 json[i].rtuName,
		   	 						             json[i].rtuOrder,
		 						                 json[i].stationZName,
		 						                 json[i].frameName,
		 						                 json[i].frameOrder,
		 						                 json[i].length,
		 						                 json[i].status,
		 						                 json[i].isUplink,
		 						                 json[i].description,
		 						                 json[i].createTime,
		 						                 json[i].createUser,
		 						                 json[i].alterUser,
		 						              ];
    	 							routePara.push(equiPara)
    	 					}
    	 					   setTable(routePara);//分页写入表格
    						
    				   }
    			         
    				 },
    			 
    	     });
    }
    
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
    	var routeId=this.children[1].innerHTML;
    	$(this).addClass("currtr").siblings().removeClass("currtr");
    	if(routeId!=""){
    		$(this).addClass("route").siblings().removeClass("route");
    	}
    	else{
   		 $(this).removeClass("route").siblings().removeClass("route");
   	   }
    	var menu = new BootstrapMenu('.route', {
                actions: [{
                    name: '修改光路',
                    onClick: function() {
                    	$(this).remove();
                    	$(".sidebarDiv").load("html/resource/pathModify.html");
                    }
                },
                {
	                name: '设置优化参数',
	                onClick: function() {
	                	$(this).remove();
	                	$(".sidebarDiv").load("html/resource/pathOptSetting.html");
	                }
                },
                {
	                name: '设置光功率门限',
	                onClick: function() {
	                	$(this).remove();
	                	$(".sidebarDiv").load("html/resource/pValueThreshold.html");
	                }
                },
                {
                    name: '删除光路',
                    onClick: function() {
                    	$(this).remove();
                    	 var txt= "删除光路后您将不能继续对其检测，同时将删除该光路的曲线数据！！";
          	    	     txt+="请谨慎选择，确认继续请点击确定键";
          			     var option = {
          						title: "提示",
          						btn: parseInt("00011",2),
          						onOk: function(){//点击确认的执行方法
          							delRoute(routeId);
          						}
          					}
          			  window.wxc.xcConfirm(txt, "info", option);
                    }
                }]
            });
        
        
    }) 
    

    $(".pathModify").click(function(){
        var trs = document.querySelectorAll("#routeTableDetail tbody tr");
	    var selectFlag=0;
	    for (var tableCount=0;tableCount<trs.length;tableCount++){
	     if($(trs[tableCount]).hasClass("currtr")){
	    	 if(trs[tableCount].children[1].innerHTML!=""){//标识不为空，表明一定选中，而且选中的部位空
	    	    selectFlag=1;
	    	    tableCount=trs.length;
	    	 }
	      }
	    }
	    if(selectFlag==0)//未选择route
		   {
	    	        var txt= "您尚未选择任何光路，请先选择您要修改的光路<br/>"
					var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){
							    	
								  }
							}
					  window.wxc.xcConfirm(txt, "info", option);
		   }
	    else{
	        $(".rightTable").show();
	        $(".pathRouteCon").hide();
	        $(".sidebarDiv").load("html/resource/pathModify.html");
	    } 

    });
function isPerToSetRoute(){
     $.ajax({
		 url:"isPerToSetRoute",
    	 dataType:'json', //接受数据格式
         async: false,
         data:null,
		 success: function(json){
			 if(json[0].status){
				    var txt= "设置方法:"
					   +"先在地图上选择起始局站，若不跨段，则直接可以进行下一步操作；"
					   +"若存在跨段，则依次在地图上选择途经的站点，选择完成后进行下一步操作。";
				     var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){
									/**显示GIS**/
								    $(".dynamicSupernatant").height(document.documentElement.clientHeight);
							    	$(".dynamicSupernatant").width(document.documentElement.clientWidth);
							        $(".dynamicSupernatant").show();
							        $("#supernatantArea").css({"width":"95%" ,"height":"90%" ,"backgroundColor":"#f9f9f9","margin":"0 auto","positon":"absolute","margin-top":"-2%"});
							        $("#supernatant").height($("#supernatantArea").height()-30);
							    	$("#supernatant").width("99.6%");
							    	$("#supernatant").css("margin-right","5px");
							    	$("#supernatant").attr("src","html/resource/mapSetRoute.html");
							  }
						}
				     window.wxc.xcConfirm(txt, "info", option);
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
  /*--------------------设置光路------------------------------*/
   $(".pathSetting").click(function(){
	  isPerToSetRoute();
	    
	 });

   /**--------------------删除光路------------------------------*/
    $(".pathDelete").click(function(){   	   
   	var trs = document.querySelectorAll("#routeTableDetail tbody tr");
	    var routeId="";
	    for (var tableCount=0;tableCount<trs.length;tableCount++){
	     if($(trs[tableCount]).hasClass("currtr")){
	    	  routeId=trs[tableCount].children[1].innerHTML;
	    	  tableCount=trs.length;
	      }
	    }if(routeId=="")//未选择
		   {
	    	   var txt= "您尚未选择任何光路，请先选择您要删除的光路\n";
			   var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
		   }
	    else{
	    	   var txt= "删除光路后您将不能继续对其检测，同时将删除该光路的曲线数据<br/>";
	    	   txt+="请谨慎选择，确认继续请点击确定键";
			   var option = {
						title: "提示",
						btn: parseInt("00011",2),
						onOk: function(){//点击确认的执行方法
							delRoute(routeId);
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
	       
	    }    	    	
    });
    /**
     * 删除光路**/
    function delRoute(routeId){
    	 $.ajax({
 		    url:'route/delRoute',
		        type:'post', //数据发送方式
		        dataType:'json', //接受数据格式
		        async: false,
		        data:{
		        	  "routeId":routeId
		        	},
		        success: function(json){
		       	        var txt="";
				    	if(json[0].status){
				    		txt+="删除成功<br/>"
				    	}
				    	else{
				    		txt+="删除失败，请重试<br/>";
				    		txt+="失败原因："+json[0].err;
				    	}
					     var option = {
									title: "提示",
									btn: parseInt("0001",2),
									onOk: function(){//点击确认的执行方法
										getRouteByRtuId(json[0].rtuId);
									}
								}
						  window.wxc.xcConfirm(txt, "info", option);
				      },
				    error:function(XMLHttpRequest,Error)
				      {
				    	   
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
    	 })
    }
/*----------优化测试参数设置-------------*/
    $(".pathOptSetting").click(function(){
        var trs = document.querySelectorAll("#routeTableDetail tbody tr");
 	    var selectFlag=0;
 	    for (var tableCount=0;tableCount<trs.length;tableCount++){
 	     if($(trs[tableCount]).hasClass("currtr")){
 	    	 if(trs[tableCount].children[1].innerHTML!="")//标识不为空，表明一定选中，而且选中的部位空
 	    	  selectFlag=1;
 	      }
 	    }if(selectFlag==0)//未选择route
 		   {
 	    	   var txt= "您尚未选择任何光路，请先选择您要设置参数的光路\n";
			   var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
 		   }
 	    else{
 	    	$(".sidebarDiv").load("html/resource/pathOptSetting.html");
 	    } 
    });
    /**--------查询光路---------*/
    $(".routeSearch").click(function(){
    	var name=$("#routeName").val();
		if(name==""){    //未输入查询信息
			   var txt= "您尚未输入任何查询条件，请先输入查询条件\n";
			   var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
						}
					}
			  window.wxc.xcConfirm(txt, "info", option);
		}
		else{
			$("#routeName").val("");
	    	$.ajax({
	    		     url:"routeSearch",
			    	 dataType:'json', //接受数据格式
		             async: false,
		             data:{
		                     "name":name,
		                  },
					 success: function(json){
						 if(json[0].status){
								 var tds = document.querySelectorAll("#routeTableDetail tbody tr td");
							 	    for (var i=0;i<tds.length;i++){
							 	        tds[i].innerHTML = "";
							 	    }
							 	   
							 	   var routePara=[]; 
			 					 	for(var i=1;i<json.length;i++){
			 							   
			 							   var equiPara=[
			 							                 	 json[i].id,
			    	 						                 json[i].name,
				    	 						             json[i].stationAName,
				   	 						                 json[i].rtuName,
					   	 						             json[i].rtuOrder,
					 						                 json[i].stationZName,
					 						                 json[i].frameName,
					 						                 json[i].frameOrder,
					 						                 json[i].length,
					 						                 json[i].status,
					 						                 json[i].isUplink,
					 						                 json[i].description,
					 						                 json[i].createTime,
					 						                 json[i].createUser,
					 						                 json[i].alterUser,
				 						              
			 							                ];
			 							  
			 							  routePara.push(equiPara)
			 						}
			 					   setTable(routePara);//分页写入表格
					     }
						 else{    //不存在查找项
							   var txt= "查找失败，当前局站不存在满足条件的光路\n";
							   var option = {
										title: "提示",
										btn: parseInt("0001",2),
										onOk: function(){//点击确认的执行方法
										}
									}
							  window.wxc.xcConfirm(txt, "info", option);
						 }
				         
					 },
	    	 });
		}
     })
    