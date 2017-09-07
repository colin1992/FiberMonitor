 /**全局变量**/
        /**当前页码**/
        var currentPage=1;
        /**当前选中的光路Id**/
        var currentRouteId=1;
        /**当前表格的列数**/
        var perCount=document.querySelectorAll(".warningQueryTableDetail tbody tr").length;
        /**跳转到指定页**/
        function gotoPointPage(){
       	 var pointPage=$("#pointPage").val();
       	 var isValidate=false;
       	 if(!isNaN(pointPage)){//必须为数字
       		 //必须在合理的页码范围内
       		 if(parseInt(pointPage)<=parseInt($("#totalPage").text())&&parseInt(pointPage)>0){
       			 currentPage=parseInt(pointPage);
       			 getWarnsByPage(currentPage);
       			 $("#currentPage").text(currentPage);
       			 isValidate=true;
       		 }
       	 }
       	 //不合法输入
       	 if(!isValidate){
       		    var txt="您输入的页码有误，请输入合法的页码，跳转页码必须为整数且不超过总页码数。";
       	    	var option={
       	    	   					title: "提示",
       	    	   					btn: parseInt("0001",2),
       	    	   					onOk: function(){//点击确认的执行方法
       	    	   						
       	    	   					},
       	    	  			       
       	    	   				}
       	    	  window.wxc.xcConfirm(txt, "info", option); 
       	 }
        }
        /**上一页**/
        function gotoPrePage(){
       	if(currentPage>1){//页码必须不小于1
       		currentPage-=1;
       		$("#currentPage").text(currentPage);
       		getWarnsByPage(currentPage);
       		
       	}
       	else{
       		    var txt="当前页已为首页。";
       	    	var option={
       	    	   					title: "提示",
       	    	   					btn: parseInt("0001",2),
       	    	   					onOk: function(){//点击确认的执行方法
       	    	   						
       	    	   					},
       	    	  			       
       	    	   				}
       	    	  window.wxc.xcConfirm(txt, "info", option); 
       	}
        }
        /**下一页**/
        function gotoNextPage(){
       	if(currentPage<parseInt($("#totalPage").text())){//页码最大为尾页
       		currentPage+=1;
       		$("#currentPage").text(currentPage);
       		getWarnsByPage(currentPage);
       		
       	}
       	else{
       		    var txt="当前页已为尾页。";
       	    	var option={
       	    	   					title: "提示",
       	    	   					btn: parseInt("0001",2),
       	    	   					onOk: function(){//点击确认的执行方法
       	    	   						
       	    	   					},
       	    	  			       
       	    	   				}
       	    	  window.wxc.xcConfirm(txt, "info", option); 
       	}
        }
       	 /**首页**/
       	function gotoFirstPage(){
       			currentPage=1;
       			getWarnsByPage(currentPage);
       			$("#currentPage").text(currentPage);
        }
       	 /**尾页**/
       	function gotoLastPage(){
       			currentPage=parseInt($("#totalPage").text());
       			getWarnsByPage(currentPage);
       			$("#currentPage").text(currentPage);
        }
        /**通过指定页码和每页的条目数获取指定页的告警条目**/
        function getWarnsByPage(page){
       	 $.ajax({
       			type : "post",
       			async : false,  //异步请求 先执行后续操作，再执行sucess
       			url : "getWarnsByPage",
       			dataType:"json",
       			data : {
       					'page':page,
       					'perCount':perCount,
       					'routeId':currentRouteId,
       			},
       			success:function(json){
       			    var data=json[0].alarms;
       			    var dataInput=[]; 
       				for(i=0;i<data.length;i++){
       					dataInput.push(
         						[
         						    data[i].id,
      				                data[i].type,
      				                data[i].source,
      				      		    data[i].routeName,
      				      		    data[i].level,
      				      		    data[i].distance,
      				    			data[i].alarmTime,
      							    data[i].isHandle,
      							    data[i].handleUser,
      							    data[i].handleTime
      							 ]);                   			
         				}
       				setWarnTable(dataInput);  
       			},
       		});
        }

$("#backToGis").click(function(){
		$(".rightTable").css("display","none");
		$(".mapDiv").css("display","block");
	})	
//左侧树的形成
	$(document).ready(function(){
	  var hrefReload="javascript:Reload()";
	  leftTree = new dTree('leftTree');//创建一个对象.
	  leftTree.config.folderLinks=true;
	  leftTree.config.useCookies=false;
	  leftTree.config.check=true;
	  $.ajax({
	      url:'curveTree',
	      type:'post', //数据发送方式
	      dataType:'json', //接受数据格式
	      async: false,
	      success: function(json){
	          for(var JsonCount=0;JsonCount<json.length;JsonCount++){
              	var equiId="";//定义字符串来承接
                var NodeId=json[JsonCount].id;//读取节点id
              	var nodeName=json[JsonCount].name;
                  var parentId=json[JsonCount].pid;//读取节点pid
                  if(parseInt(NodeId[0])<5){  //显示到光路
                      if(NodeId=="0"||NodeId==0){
                        	leftTree.add(NodeId,parentId,"告警管理",hrefReload,"","","","",true);
                         }
                       else if(parseInt(NodeId[0])==4){//为RTU
                          	 for(var index=2;index<NodeId.length;index++){ //
                          		equiId+=NodeId[index];//拼接字符串
                          	 }
                          	hrefStaAddress="javascript:getWarnByRouteId("+parseInt(equiId)+")";//
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
              error:function(XMLHttpRequest,Error)
	       	  {            	       	 
	       	   }
          });
	  
  });
	
	 /**--------------------点击告警管理节点，重载当前页面---------------------------*/
	    function Reload(){
	    	$(".containerDiv").load("html/warn/warnQuery.html");
	    }
	    
	   
    /**--------根据RTU Id获取告警信息----------**/
       function getWarnByRouteId(routeId){
		    	$(".rightTable").css("display","block");
		    	$(".mapDiv").css("display","none");
		    	$("#pagination").css('display','block');
				$("#currentPage").text(currentPage);
		    	currentRouteId=routeId;
				var dataInput=[];            	    
		      	$.ajax({
              			type : "post",
              			async : false,  //异步请求 先执行后续操作，再执行sucess
              			url : "alarm/getWarn",
              			dataType:"json",
              			data : {
              						"routeId":routeId,
              						'perCount':perCount,
              				   },
              			success:function(json){
              				if(json[0].status){
              				   //写入总页数
                   			   $("#totalPage").text(json[0].pageCount);
                   			   $("#pointPage").attr("max",json[0].pageCount);
              					var data=json[0].alarms;
              					for(i=0;i<data.length;i++){
                     				dataInput.push([
                     				                data[i].id,
                       				                data[i].type,
                       				                data[i].source,
                       				      		    data[i].routeName,
                       				      		    data[i].level,
                       				      		    data[i].distance,
                       				    			data[i].alarmTime,
                       							    data[i].isHandle,
                       							    data[i].handleUser,
                       							    data[i].handleTime
                       							    ]);  
                     			 }
              					setWarnTable(dataInput); 	
              				}              				
              			},
              		});      	
  }
 /**
  * 查询告警
  * **/  
    $("#checkAlarm").click(function(){
    	setWarnTable([]);//查询前先将表格清空
    	$.ajax({
  			type : "post",
  			async : false, //
  			url : "alarm/checkAlarm",
  			dataType:"json",
  			data : {
  				      "routeId":currentRouteId,
  				      'perCount':perCount,
  					  "status":$("#status").val(),
  					  "startTime":$("#startTime").val(),
  					  "endTime":$("#endTime").val(),
  				   },
  			success:function(json){
  				if(json[0].status){
  				    //写入总页数
        			$("#totalPage").text(json[0].pageCount);
        			$("#pointPage").attr("max",json[0].pageCount);
  					var data=json[0].alarms;
  					var dataInput=[];
  					for(i=0;i<data.length;i++){
         				dataInput.push([
										   data[i].id,
										   data[i].type,
										   data[i].source,
										   data[i].routeName,
										   data[i].level,
										   data[i].distance,
										   data[i].alarmTime,
										   data[i].isHandle,
										   data[i].handleUser,
										   data[i].handleTime
           							    ]);  
         			 }
  					setWarnTable(dataInput); 	
  				}
  				else{
  				     var txt= "查找失败，不存在满足条件的告警信息。"
  					 var option = {
  								title: "提示",
  								btn: parseInt("0001",2),
  								onOk: function(){  							    	
  								  }
  							}
  					  window.wxc.xcConfirm(txt, "info", option);
  				}
  			},
  		    error:function(XMLHttpRequest,Error)
		    {
		    	  var txt="查找失败,";
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
    })   
    //开始时间和结束时间的限制
	var start = {
	    elem: '#startTime',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    max: laydate.now(), //最大日期
	    istime: true,
	    festival:true,
	    choose: function(datas){
	         end.min = datas; //开始日选好后，重置结束日的最小日期
	         end.start = datas //将结束日的初始值设定为开始日
	    }
	};
	var end = {
	    elem: '#endTime',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    max: laydate.now(),
	    istime: true,
	    festival:true,
	    choose: function(datas){
	        start.max = datas; //结束日选好后，重置开始日的最大日期
	        start.end = datas;
	    }
	};
	laydate(start);
	laydate(end);
	
/************************实现分页的表格写入函数***************************/
    function setWarnTable(tableData){
    	 var trs = document.querySelectorAll("#warningQueryTableDetail tbody tr");
         var tds = document.querySelectorAll("#warningQueryTableDetail tbody tr td");
         var cells = document.getElementById("warningQueryTableDetail").rows.item(0).cells.length;
         var index=(currentPage-1)*perCount;
        //写入前先清空
         for (var i=0;i<tds.length;i++){
             tds[i].innerHTML = "";
        }
        for (var i=0; i<tableData.length;i++) {
              var Ele = trs[i].children;
              for(var j=0; j<cells-1; j++){
                   Ele[0].innerHTML=i+1+index;
                   Ele[j+1].innerHTML=tableData[i][j];
               }
        }
    }
      
    /**表格范围内禁止系统右键，使用自定义右键**/
    $("#warningQueryTableDetail").hover(function(){
    	 $(document).bind("contextmenu",function(e){   
    		   return false;   
    		 });
    },
    function(){
    	$(document).unbind("contextmenu","")
    });	
    /****************************添加右键菜单功能***********************************/
    $(".warningQueryTableDetail tbody tr").click(function(){
    	 $(this).addClass("currtr").siblings().removeClass("currtr");
    	 var selectId=this.children[1].innerHTML;
    	 if(selectId!=""){
    		 $(this).addClass("warmQuery").siblings().removeClass("warmQuery");
    	 }
    	var menu = new BootstrapMenu('.warmQuery', {
  		  actions: [{
              name: '邮件处理',
              onClick: function() {
            	  $(this).remove();
            	  $(".sidebarDiv").load("html/warn/warnemail.html");
              }
          },
          {
              name: '短信处理',
              onClick: function() {
            	  $(this).remove();
            	  $(".sidebarDiv").load("html/warn/warnphone.html");
              }
          },
          {
          	name: '忽略告警',
              onClick: function() {
            	$(menu).remove();
              	$.ajax({
                      type : "post",
                      async : false,  //异步请求 先执行后续操作，再执行sucess
                      url : "alarm/handleAlarm/ignore",
                      dataType:"json",
                      data : {
                      		"warnId":selectId,
                      },
                      success:function(json){
                        $(".sidebarDiv").html("");
                        var txt="";
       			    	 if(json[0].status){
       			    		txt+="处理成功.<br/>"
       			    	}
       			    	else{
       			    		txt+="处理失败，请重试";
       			    	}
       				     var option = {
       								title: "提示",
       								btn: parseInt("0001",2),
       								onOk: function(){//点击确认的执行方法
       									getWarnByRouteId(json[0].routeId);//通过RTU id刷新表格  该方法在warnQuery.js中
       								}
       							}
       					  window.wxc.xcConfirm(txt, "info", option);
       			      },
       			    error:function(XMLHttpRequest,Error)
       			      {
       			    	    $(".sidebarDiv").html("");
       			    	    var txt="处理告警失败<br/>";
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
          },
          {
            	name: '删除告警',
                onClick: function() {
              	$(menu).remove();
                	$.ajax({
                        type : "post",
                        async : false,  //异步请求 先执行后续操作，再执行sucess
                        url : "alarm/delAlarm",
                        dataType:"json",
                        data : {
                        		"warnId":selectId,
                        },
                        success:function(json){
                          $(".sidebarDiv").html("");
                          var txt="";
         			    	 if(json[0].status){
         			    		txt+="删除成功.<br/>"
         			    	}
         			    	else{
         			    		txt+="处理失败，请重试。";
         			    	}
         				     var option = {
         								title: "提示",
         								btn: parseInt("0001",2),
         								onOk: function(){//点击确认的执行方法
         									getWarnByRouteId(json[0].routeId);//通过RTU id刷新表格  该方法在warnQuery.js中
         								}
         							}
         					  window.wxc.xcConfirm(txt, "info", option);
         			      },
         			    error:function(XMLHttpRequest,Error)
         			      {
         			    	    $(".sidebarDiv").html("");
         			    	    var txt="删除告警失败<br/>";
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
            }
		  ]
      });
    }) 

//载入这个页面的时候，首先呈现的是GIS界面
	$(".rightTable").css("display","none");
	$(".mapDiv").css("display","block");


function openSpeficTree(stationId){
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	leftTree.config.folderLinks=false;
	leftTree.closeAll();
	var stationid="2_"+stationId;
	leftTree.openTo(stationid,false);
}
