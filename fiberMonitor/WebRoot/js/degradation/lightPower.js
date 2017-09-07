var timeTicket=null;
/**全局变量 光功率值**/
var powerValueData=[];
/**全局变量  光功率门限值**/
var pTh=[];
/**定义光路ID的全局变量**/
var routeID = 0;
/**离开页面时停止获取**/
$(window).bind('beforeunload',function(){
  
}); 
//左侧树的形成
$(document).ready(function(){
   var hrefCable="javascript:Reload()";
   var hrefStaAddress="";//定义url指向js，当为区域时需要使用
   leftTree = new dTree('leftTree');//创建一个对象.
   leftTree.config.folderLinks=true;
   leftTree.config.useCookies=false;
   leftTree.config.check=true;
    $.ajax({
             url:'lightTree',
             type:'post', //数据发送方式
             dataType:'json', //接受数据格式
             async: false,
			 success: function(json){
                    	for(var cableJsonCount=0;cableJsonCount<json.length;cableJsonCount++){
                    		 var equiId="";//定义字符串来承接stationId
                    		 var NodeId=json[cableJsonCount].id;//读取节点id
                    		 var nodeName=json[cableJsonCount].name;
                    		 var parentId=json[cableJsonCount].pid;//读取节点pid
                             if(parseInt(NodeId[0])<5){  //显示到光路
                            	 if(NodeId=="0"||NodeId==0){
                              		leftTree.add(NodeId,parentId,"光功率实时显示",hrefCable,"","","","",true);
                                    }
                            	 else if(parseInt(NodeId[0])==4){//为光路
	                            		 for(var index=2;index<NodeId.length;index++){ //
	                            			 equiId+=NodeId[index];//拼接字符串
	                            		} 
	                            		 hrefStaAddress="javascript:getRoutePoint('"+parseInt(equiId)+"')";//
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




//载入这个页面的时候会有横纵坐标
//载入这个页面的时候会有横纵坐标
var myChart = echarts.init(document.getElementById("container")); 
option = {
		    title: {
		        text: '光功率实时显示'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            dataView : {
		            	 		show: true, 
		            	        readOnly: false,
		            	        
		            	        },
		            saveAsImage : {show: true},
		            dataZoom: {
	                    yAxisIndex: 'none'
	                    	},
	                restore: {}, 
		        }
		    },
		    tooltip: {
		        trigger: 'axis',
		        formatter: function (params) {
		            params = params[0];
		            var date = new Date(params.name);
		            return date.getHours() + ':' + date.getMinutes()  + ':' + date.getSeconds() + ' _ ' + params.value[1];
		        },
		        axisPointer: {
		            animation: true
		        }
		    },
		   legend: {
	           data:['光功率实时显示','阈值一','阈值二','阈值三','阈值四'],
	           selected: {
                '阈值一': false, '阈值二': false, '阈值三': false, '阈值四': false
 	           }
	        },
		    xAxis: {
		        type: 'time',
		        name: '时间',
		        splitLine: {
		            show: true
		        }
		    },
		    yAxis: {
		        type: 'value',
		        name: '光功率值(db)',
		        boundaryGap: [0, '100%'],
		        splitLine: {
		            show: true
		        }
		    },
		    series: []
		};	
myChart.setOption(option);
//重新加载本页
   function Reload(){
         $(".containerDiv").load("html/degradation/lightPower.html");
    }

   function getRoutePoint(routeId) {
	   powerValueData=[];//每次点击光路先清空获得的历史数据
	   $(".rightTable").css("display","block");
	   $(".mapDiv").css("display","none");
       routeID = routeId;
}
/**时间输入框只能输入数字**/  
   $("#interval").focus(function(){
	   $(this).keypress(function (event) {
           var eventObj = event || e;
           var keyCode = eventObj.keyCode || eventObj.which;
           if ((keyCode >= 48 && keyCode <= 57))
               return true;
           else
               return false;
	  	})
   })
  /**根据光路Id获取光功率门限值**/
  function  getPowerThre(routeID){
	   $.ajax({
           type : "post",
           async : false,  //异步请求 先执行后续操作，再执行sucess
           url : "degradation/getPth",
           dataType:"json",
           data : {
             	"routeId":routeID
           },
           success:function(json){
	           	if(json[0].status){
	           		pTh=json[0].pTh;//门限值
	           	}
	           	else{
	           	  pTh=[-70,-40,-30,-20];
	           	  var txt= "当前光路未配置光功率门限，将使用默认门限，您可以点击\"门限参数设置\"按键配置门限参数。";
				  var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
						 }
						}
				  window.wxc.xcConfirm(txt, "info", option);
	           	}
           },
	   })  
   }
$("#settingConfirm").click(function() {
	    if(routeID==0){
	    	  var txt= "请先选择您要查看光功率的光路.";
			  var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
					 }
					}
			  window.wxc.xcConfirm(txt, "info", option);
	    }
	    else if($("#interval").val()==""){
	    	  var txt= "请输入光功率数据采集间隔.";
			  var option = {
						title: "提示",
						btn: parseInt("0001",2),
						onOk: function(){//点击确认的执行方法
					 }
					}
			  window.wxc.xcConfirm(txt, "info", option);
	    }
	   else{
	     if($("#settingConfirm").text()=="停止"){
	    	 clearInterval(timeTicket);
 	    	 timeTicket=null;//关闭采集
	    	 $("#settingConfirm").text("获取光功率");
	     } 
	    else{
	    	$("#settingConfirm").text("停止");
	    	getPowerThre(routeID);//获取光功率门限
	    	var timeInterval = $("#interval").val();
			var unit = $("#unit").val();
			if(unit=="s")
				//timeInterval=500;
				timeInterval = timeInterval*1000;
			else 
				timeInterval = timeInterval*60*1000;
				
			 //光功率实时显示
			   var myChart = echarts.init(document.getElementById("container"));  
			   
			 //获取数据
			   function randomData(powerTime,powerData) {
				   
			   	    return {
			   	        name: powerTime,
			   	        value: [
			   	            powerTime, 
			   	            Math.round(powerData*100)/100
			   	        ]
			   	    }
			   	}		   		   		
			   		
			   		var thresholdData1 = [];
			   		var thresholdData2 = [];
			   		var thresholdData3 = [];
			   		var thresholdData4 = [];
			   		var value = Math.random() * 1000;
			   		option = {
			   		    title: {
			   		        text: '实时光功率采集'
			   		    },
			   		    toolbox: {
			   		        show : true,
			   		        feature : {
			   		            dataView : {
			   		            	show: true, 
			   		            	readOnly: true,
			   		            	optionToContent: function(opt) {
			                            var axisData = powerValueData;//
			                            var series = opt.series;
			                            var table = '<table style="width:100%;text-align:center"><tbody><tr>'
			                                         + '<td>时间</td>'
			                                         + '<td>' + series[0].name + '</td>'
			                                         + '<td>' + "一级门限" + '</td>'
			                                         + '<td>' + "二级门限" + '</td>'
			                                         + '<td>' + "三级门限" + '</td>'
			                                         + '<td>' + "四级门限" + '</td>'
			                                         + '</tr>';
			                            for (var i = 0, l = axisData.length; i < l; i++) {
			                                table += '<tr>'
			                                         + '<td>' + axisData[i].name + '</td>'
			                                         + '<td>' + series[0].data[i].value[1] + '</td>'
			                                         + '<td>' + series[1].data[i].value[1] + '</td>'
			                                         + '<td>' + series[2].data[i].value[1] + '</td>'
			                                         + '<td>' + series[3].data[i].value[1] + '</td>'
			                                         + '<td>' + series[4].data[i].value[1] + '</td>'
			                                         + '</tr>';
			                            	}
			                            table += '</tbody></table>';
			                            return table;
			                        	}
			   		            },
			   		            saveAsImage : {show: true},
			   		            dataZoom: {
			 	                    yAxisIndex: 'none'
			 	                    	},
			 	                restore: {}, 
			   		        }
			   		    },
			   		 dataZoom: [{
			   			    type: 'inside',
					 }],
                    tooltip: {
			             trigger: 'axis'
			         },
			   		   legend: {
			 	           data:['实时光功率值','阈值一','阈值二','阈值三','阈值四'],
			 	           selected: {
				                '阈值一': false, '阈值二': false, '阈值三': false, '阈值四': false
				 	           }
			 	        },
			   		    xAxis: {
			   		        type: 'time',
			   		        name: '时间',
			   		        splitLine: {
			   		            show: true
			   		        }
			   		    },
			   		    yAxis: {
			   		        type: 'value',
			   		        name: '光功率值(db)',
			   		        boundaryGap: [0, '100%'],
			   		        splitLine: {
			   		            show: true
			   		        }
			   		    },
			   		    series: [{
			   		        name: '实时光功率值',
			   		        type: 'line',
			   		        showSymbol: true,
			   		        hoverAnimation: true,
			   		        data: powerValueData
			   		    },
			   		 {
		 	   		        name: '阈值一',
		 	   		        type: 'line',
		 	   		        showSymbol: false,
		 	   		        hoverAnimation: true,
		 	   		        data: thresholdData1
		 	   		      },
		 	   		  {
		 	 	   		        name: '阈值二',
		 	 	   		        type: 'line',
		 	 	   		        showSymbol: false,
		 	 	   		        hoverAnimation: true,
		 	 	   		        data: thresholdData2
		 	 	   		      },
		 	   		  {
			   		        name: '阈值三',
			   		        type: 'line',
			   		        showSymbol: false,
			   		        hoverAnimation: true,
			   		        data: thresholdData3
			   		      },
		 	   		{
			   		        name: '阈值四',
			   		        type: 'line',
			   		        showSymbol: false,
			   		        hoverAnimation: true,
			   		        data: thresholdData4
			   		      }
			   		    ]
			   		};
			   		timeTicket=setInterval(function () {
			   			//获取实时的光功率的值(与RTU进行通信)
			   			$.ajax({
			                type : "post",
			                async : false,  //异步请求 先执行后续操作，再执行sucess
			                url : "degradation/checkPowerValue",
			                dataType:"json",
			                data : {
			                	"routeId":routeID
			                },
			                success:function(json){
			                	var powerValue=0;
			                	if(json[0].status){
			                		powerValue=json[0].opticalPower.powerValue;
			                		now = new Date();
			                		if(powerValueData.length>3000){//超过一定数目后清空
			                			powerValueData=[];
			                			thresholdData1=[];
			                			thresholdData2=[];
			                			thresholdData3=[];
			                			thresholdData4=[];
			                		}
				                	powerValueData.push(randomData(now.toString(),powerValue));
				                	thresholdData1.push(randomData(now.toString(),pTh[0]));
				                	thresholdData2.push(randomData(now.toString(),pTh[1]));
				                	thresholdData3.push(randomData(now.toString(),pTh[2]));
				                	thresholdData4.push(randomData(now.toString(),pTh[3]));
			                	}
			                	else{
			                		 clearInterval(timeTicket)
			                		  $("#settingConfirm").text("获取光功率");
				        	    	 timeTicket=null;//关闭采集
			                		 var txt="光功率采集失败<br/>";
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
			                	clearInterval(timeTicket);
			                	 $("#settingConfirm").text("获取光功率");
			        	    	timeTicket=null;//关闭采集
			                	var txt="光功率采集失败<br/>";
					  			txt+="失败原因：";
					  			if(XMLHttpRequest.status==401){
					  			    txt+="您不具有当前操作的权限<br/>";
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
			   			myChart.setOption({
			   		        series: [{
			   		            data: powerValueData
			   		        },
			   		     {
			   		            data: thresholdData1
			   		        },
			   		     {
			   		            data: thresholdData2
			   		        },
			   		     {
			   		            data: thresholdData3
			   		        },
			   		     {
			   		            data: thresholdData4
			   		        }
			   		        ]
			   		    });
			   		}, timeInterval);
			   		myChart.setOption(option);
	    }	
   }
})

$("#setThresPara").click(function(){
	if(routeID!=0)
		$(".sidebarDiv").load("html/degradation/setThresholdPara.html");
})



//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display","none");
$(".mapDiv").css("display","block");

$("#backToGis").click(function(){
	$(".rightTable").css("display","none");
	$(".mapDiv").css("display","block");
})
function openSpeficTree(stationId){
	leftTree.config.folderLinks=false;
	leftTree.closeAll();
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	var stationid="2_"+stationId;
	leftTree.openTo(stationid,false);
}