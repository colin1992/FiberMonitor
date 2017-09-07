/**
 * 障碍告警测试主控js

 */
/**
 * 
 */
/*-----------全局变量----------*/
var selectCM=0;
var selectSNoAndId=[];
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
                        	leftTree.add(NodeId,parentId,"障碍告警测试",hrefReload,"","","","",true);
                         }
                      else if(parseInt(NodeId[0])==3){//为RTU
                          	 for(var index=2;index<NodeId.length;index++){ //
                          		equiId+=NodeId[index];//拼接字符串
                          	 }
                          	hrefStaAddress="javascript:getRouteByRtuId("+parseInt(equiId)+")";//
                        	leftTree.add(NodeId,parentId,nodeName,hrefStaAddress,"","","","",false);
                       }
                  
                      else
                      {
                      		leftTree.add(NodeId,parentId,nodeName,"","","","","",false);
                      }
                  }
             
              }
              document.getElementById("leftTree").innerHTML = leftTree;  
              
           },
            
          });
	  
  });
  

//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display","none");
$(".mapDiv").css("display","block");

$("#backToGis").click(function(){
	$(".rightTable").css("display","none");
	$(".mapDiv").css("display","block");
})


  /**--------------------点击光路管理节点，重载当前页面---------------------------*/
    function Reload(){
    	$(".containerDiv").load("html/curve/obstacleTesting.html");
    }
  /**-------------------点击RTU节点，获取当前RTU的光路------------------------*/
   
    function getRouteByRtuId(rtuId){
    	$(".rightTable").css("display","block");
    	$(".mapDiv").css("display","none");
    	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr td");
 	    for (var i=0;i<trs.length;i++){
 		    trs[i].innerHTML = "";
 		}//先清空
 	    $.ajax({
 		    	 url:"getObstacleRouteByRtuId",
 		    	 dataType:'json', //接受数据格式
 	             async: false,
 	             data:{
 	                     "id":rtuId, 
 	                  },
 				 success: function(json){
 					 if(json[0].status){
 						 var tds = document.querySelectorAll("#obstacleTableDetail tbody tr td");
 					 	    for (var i=0;i<tds.length;i++){
 					 	        tds[i].innerHTML = "";
 					 	    }
 					     var routePara=[];
 					     for(var count=1;count<json.length;count++){
	 						 var equiPara=[
	 								              json[count].id,
	 								              json[count].name,
	 								              json[count].stationAName,
	 								              json[count].rtuId,
									              json[count].rtuName,
									              json[count].rtuOrder,
									              json[count].stationZName,
									              json[count].frameId,
									              json[count].frameName,
									              json[count].frameOrder,
	 								              json[count].length,
	 								              json[count].status,
	 								              json[count].isObstacle,
									              json[count].priorityId,
									              json[count].priorityName,
									              json[count].description
	 							         ];
	 								  
	 						 routePara.push(equiPara);   
					   }
 					    setTable(routePara);//分页写入表格
 				  }
 			         
 		    },
 			 
 	     });
    }
    /************************实现分页的表格写入函数***************************/
    function setTable(tableData){
        var nums = document.querySelectorAll(".obstacleTableDetail tbody tr").length;
        var cells = document.getElementById("obstacleTableDetail").rows.item(0).cells.length;
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
                var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
                var tds = document.querySelectorAll("#obstacleTableDetail tbody tr td");
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
    $("#obstacleTableDetail").hover(function(){
    	 $(document).bind("contextmenu",function(e){
    	   e.preventDefault();
  		   return false;   
  		 });
    },
    function(){
    	$(document).unbind("contextmenu","")
    });	
    /****************************添加右键菜单功能***********************************/
    $(".obstacleTableDetail tbody tr").click(function(){
    	 $(this).addClass("currtr").siblings().removeClass("currtr");
    	 var state = this.children[13].innerHTML;
    	 if(this.children[1].innerHTML!=""){
    		$(this).addClass("obstacleTest").siblings().removeClass("obstacleTest");
    	}
    	else{
    		$(this).removeClass("obstacleTest").siblings().removeClass("obstacleTest");
    	}
         selectSNoAndId=[];
         selectSNoAndId.push([getPortOrder(this.children[6].innerHTML),this.children[1].innerHTML]);
         selectCM=this.children[4].innerHTML;
    	 if(state == "否"){
        	var menu = new BootstrapMenu('.obstacleTest', {
                actions: [{
                    name: '障碍告警参数配置',
                    onClick: function() {
                    	$(this).remove();
                    	$(".sidebarDiv").load("html/curve/obstacleParaSeting.html");
                    }
                },
                {
	                name: '设置优化参数',
	                onClick: function() {
	                	$(this).remove();
	                   $(".sidebarDiv").load("html/curve/opticalParameterSetting.html");
	                }
                },
                {
                    name: '设置告警提示方式',
                    onClick: function() {
                    	$(this).remove();
                    	var dataList=getAlarmWays();
                    	var txt=  "告警方式&nbsp;&nbsp";
						window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.selectList,{
							title:"设置告警提示方式",
							listData:dataList,
							onOk:function(selectedId){
								setAlarmWay(selectedId);
							}
						});
                    }
                }]
            });
        }
        else if(state == "是"){
            var menu1 = new BootstrapMenu('.obstacleTest', {
                actions: [{
                    name: '取消障碍告警测试',
                    onClick: function() {
                       $(this).remove();	
                       var txt= "是否取消当前线路障碍告测试？";  
      				   var option = {
      							title: "提示",
      							btn: parseInt("0011",2),
      							onOk: function(){//点击确认的执行方法
      								calcelObstacle(selectCM,selectSNoAndId);
      							}
      						}
      				  window.wxc.xcConfirm(txt, "info", option);
                    }
                }, 
                {
                    name: '更新测试参数',
                    onClick: function() {
                    	$(this).remove();
                    	$(".sidebarDiv").load("html/curve/obstacleParaSeting.html");
                    }
                },
                {
                    name: '修改告警提示方式',
                    onClick: function() {
                    	$(this).remove();
                    	var dataList=getAlarmWays();
                    	var txt=  "告警方式&nbsp;&nbsp";
						window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.selectList,{
							title:"修改告警提示方式",
							listData:dataList,
							onOk:function(selectedId){
								setAlarmWay(selectedId);
							}
						});
                    }
                },
                {
                    name: '取消告警提示',
                    onClick: function() {
                    	 $(this).remove();
                    	 var txt= "您选择了取消告警提示，取消之后当前RTU" +
                    	 		"下的所有光路将不会有任何告警提示";
                    	 txt+="<br/>确认继续请点击\"确认\"键"
	      				   var option = {
	      							title: "提示",
	      							btn: parseInt("0001",2),
	      							onOk: function(){//点击确认的执行方法
	      								cancelAlarmWay();
	      							}
	      						}
	      				  window.wxc.xcConfirm(txt, "info", option); 
                    }
                }]
                });
        }
    })
   /**------------------获取全部告警方式--------------------*/
    function getAlarmWays(){
    	var list=[];
	    	 $.ajax({
		    	 url:"getAllAlarmWays",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                  },
				 success: function(json){
					 if(json[0].status){
						 list=json[1];
					   }
				 },
	    	 })
	    return list;
    }
    /**------------为光路设置告警提示方式-------------*/
    function setAlarmWay(alarmId){
    	$.ajax({
	    	 url:"setAlarmAlert",
	    	 dataType:'json', //接受数据格式
             async: false,
             data:{
                    "CM":selectCM, 
                    "alarmId":alarmId
                 },
			 success: function(json){
				 if(json[0].status){
					   var txt= "您已成功为当前RTU下光路设置告警提示";  
     				   var option = {
     							title: "提示",
     							btn: parseInt("0001",2),
     							onOk: function(){//点击确认的执行方法
     								
     							}
     						}
     				  window.wxc.xcConfirm(txt, "info", option); 
					   
				   }
				 else{
					 var txt= "设置失败，请重试"; 
					 txt+="错误原因："+json[0].err;    
     				   var option = {
     							title: "提示",
     							btn: parseInt("0001",2),
     							onOk: function(){//点击确认的执行方法
     								
     							}
     						}
     				  window.wxc.xcConfirm(txt, "info", option); 
				 }
		         
			 },
			 error:function(XMLHttpRequest,textStatus){
				 var txt= "取消失败<br/>";
				 txt+="失败原因：";
				 if(XMLHttpRequest.status==401){
		  			    txt+="您不具有当前操作的权限<br/>请联系管理员获得权限";
		  			}
		  		 else if(XMLHttpRequest.status==0){
		  				txt+="连接超时";
		  			}
		  		else{
  			    	txt+="网络错误<br/>";
  			    	txt+="状态码："+XMLHttpRequest.status;
  			     }
			       var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
				},
		 
    });
    }
    /**---------------取消当前RTU的告警提示----------------*/
    function cancelAlarmWay(){
    	$.ajax({
	    	 url:"cancelAlarmAlert",
	    	 dataType:'json', //接受数据格式
             async: false,
             data:{
            	    "CM":selectCM
                 },
			 success: function(json){
				 if(json[0].status){
					   var txt= "您已成功取消当前RTU下光路的告警提示。";  
     				   var option = {
     							title: "提示",
     							btn: parseInt("0001",2),
     							onOk: function(){//点击确认的执行方法
     								
     							}
     						}
     				  window.wxc.xcConfirm(txt, "info", option); 
					}
				 else{
					 var txt= "取消失败，请重试"; 
					 txt+="错误原因："+json[0].err;    
     				   var option = {
     							title: "提示",
     							btn: parseInt("0001",2),
     							onOk: function(){//点击确认的执行方法
     								
     							}
     						}
     				  window.wxc.xcConfirm(txt, "info", option); 
				 }
		         
			 },
			 error:function(XMLHttpRequest,textStatus){
				 var txt= "取消失败。";
				 txt+="失败原因：";
				 if(XMLHttpRequest.status==401){
		  			    txt+="您不具有当前操作的权限，请联系管理员获得权限";
		  			}
		  		 else if(XMLHttpRequest.status==0){
		  				txt+="连接超时";
		  			}
		  		else{
  			    	txt+="网络错误，";
  			    	txt+="状态码："+XMLHttpRequest.status;
  			     }
			       var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
				},
		 
    });
    }
    
    /**-------------------取消障碍告警设置--------------*/
    function calcelObstacle(CM,selectSNoAndId){
    	
    	 $.ajax({
		    	 url:"curve/obstacleTest/cancelObstacle",
		    	 dataType:'json', //接受数据格式
	             async: false,
	             data:{
	                     "CM":CM, 
	                     "SNoAndId":JSON.stringify(selectSNoAndId),
	                  },
				 success: function(json){
					 getRouteByRtuId(CM);
					 if(json[0].status){
						   var txt= "您已成功取消当前光路的障碍告警测试。";  
	      				   var option = {
	      							title: "提示",
	      							btn: parseInt("0001",2),
	      							onOk: function(){//点击确认的执行方法
	      								
	      							}
	      						}
	      				  window.wxc.xcConfirm(txt, "info", option); 
						   
					   }
					 else{
						 var txt= "取消失败，请重试，"; 
						 txt+="失败原因："+json[0].err;    
	      				   var option = {
	      							title: "提示",
	      							btn: parseInt("0001",2),
	      							onOk: function(){//点击确认的执行方法
	      								
	      							}
	      						}
	      				  window.wxc.xcConfirm(txt, "info", option); 
					 }
			         
				 },
				 error:function(XMLHttpRequest,textStatus){
					 var txt= "取消失败，请重试";
					 txt+="失败原因：";
					 if(XMLHttpRequest.status==401){
			  			    txt+="您不具有当前操作的权限，请联系管理员获得权限";
			  			}
			  		 else if(XMLHttpRequest.status==0){
			  				txt+="连接超时。";
			  			}
			  		else{
	  			    	txt+="网络错误，";
	  			    	txt+="状态码："+XMLHttpRequest.status;
	  			     }
				       var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
									
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
					},
			 
	     });
    	 $(".sidebarDiv").html(""); 
         $(".contentDiv").html("");
    }
 
 /**-------------------优先级配置------------------*/
    $(".prioritySetting").click(function(){
    	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
	    var selectFlag=0;
	    for (var cableTableCount=0;cableTableCount<trs.length;cableTableCount++){
	     if(trs[cableTableCount].children[1].innerHTML!="")//
	    	  selectFlag=1;
	      
	    }if(selectFlag==0)//未选择
		   {
	    	 var txt= "您尚未选择RTU或当前RTU下不存在可用光路，请核对后重试";
	  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
		   }
	    else{
	    	$(".sidebarDiv").load("html/curve/prioritySet.html");
	    }
       
    });
     
/**-------------------障碍告警参数设置------------------*/
    $(".pathOptSetting").click(function(){
    	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
	    var selectFlag=0;
	    for (var cableTableCount=0;cableTableCount<trs.length;cableTableCount++){
	    	 if($(trs[cableTableCount]).hasClass("currtr")){
		    	 if(trs[cableTableCount].children[1].innerHTML!="")//标识不为空，表明一定选中，而且选中的部位空
		    	  selectFlag=1;
		      }
	      
	    }if(selectFlag==0)//未选择
		   {
	    	 var txt= "您尚未选择可用光路，请线选择光路";
	  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
		   }
	    else{
            $(".sidebarDiv").load("html/curve/obstacleParaSeting.html");
        }
    });
    /**--------取消障碍告警测试---------*/
    $(".cancelObstacleTesting").click(function(){
    	var trs = document.querySelectorAll("#obstacleTableDetail tbody tr");
	    var selectFlag=0;
	    for (var cableTableCount=0;cableTableCount<trs.length;cableTableCount++){
	     if(trs[cableTableCount].children[1].innerHTML!="")//
	    	  selectFlag=1;
	      
	    }if(selectFlag==0)//未选择
		   {
	    	 var txt= "您尚未选择RTU或当前RTU下不存在可用光路，请核对后重试";
	  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
		   }
	    else{
    	   $(".sidebarDiv").load("html/curve/cancelObstacleTesting.html");
    	 }
    })

    /**--------查询光路---------*/
    $(".routeSearch").click(function(){
    	var name=$("#routeName").val();
		if(name==""){    //未输入查询信息
			var txt= "您尚未输入任何查询条件，请输入光路条件";
	  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
	        
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
								 var tds = document.querySelectorAll("#obstacleTableDetail tbody tr td");
							 	    for (var i=0;i<tds.length;i++){
							 	        tds[i].innerHTML = "";
							 	    }
							 	   var routePara=[];
							 	   for(var count=1;count<json.length;count++){
	 								   var equiPara=[
	 								                  json[count].id,
	 	 								              json[count].name,
	 	 								              json[count].stationAName,
	 	 								              json[count].rtuId,
	 									              json[count].rtuName,
	 									              json[count].rtuOrder,
	 									              json[count].stationZName,
	 									              json[count].frameId,
	 									              json[count].frameName,
	 									              json[count].frameOrder,
	 	 								              json[count].length,
	 	 								              json[count].status,
	 	 								              json[count].isObstacle,
	 									              json[count].priorityId,
	 									              json[count].priorityName,
	 									              json[count].description
	 								            ];
	 								   
	 								  routePara.push(equiPara);     
								   }
							 	setTable(routePara);   
					     }
						 else{    //不存在查找项
							 var txt= "查找失败，当前节点下不存在满足条件的光路，请核对后重试";
					  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
					         
						 }
						 $("#routeName").val("");
					 },
					 error:function(){
						 var txt= "您尚未输入任何查询条件，请输入光路条件";
				  	     window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
					 }
	    	 });
		}
     })

/**GIS相关功能**/

function openSpeficTree(stationId){
	leftTree.config.folderLinks=false;
	leftTree.closeAll();
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	var stationid="2_"+stationId;
	leftTree.openTo(stationid,false);
}     