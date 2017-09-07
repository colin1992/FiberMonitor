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
                              		leftTree.add(NodeId,parentId,"光功率劣化分析",hrefCable,"","","","",true);
                                    }
                            	 else if(parseInt(NodeId[0])==4){//为光路
	                            		 for(var index=2;index<NodeId.length;index++){ //
	                            			 equiId+=NodeId[index];//拼接字符串
	                            		} 
	                            		 hrefStaAddress="javascript:clickRoute('"+parseInt(equiId)+"')";//
	                          	   leftTree.add(NodeId,parentId,nodeName,hrefStaAddress,"","","","",false);
	                          	 }
                            	 else{
                                	 leftTree.add(NodeId,parentId,nodeName,"","","","","",false);
                                  }
                            }
                    	 }
                  document.getElementById("leftTree").innerHTML = leftTree;  	
                 },
                    error:function(XMLHttpRequest,Error)
      	       	  {
                    // alert("访问出错了！！"+"\n请求状态："+XMLHttpRequest.status+"\n错误原因："+Error);
      	       	 
      	       	   }
                });
                
}); 
//重新加载本页
function Reload(){
      $(".containerDiv").load("html/degradation/degradationAnalyse.html");
 }
//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display","none");
$(".mapDiv").css("display","block");

$("#backToGis").click(function(){
	$(".rightTable").css("display","none");
	$(".mapDiv").css("display","block");
})

//载入这个页面的时候会有横纵坐标
var myChart = echarts.init(document.getElementById("container")); 
option = {
		    title: {
		        text: ''
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            dataView : {show: true, readOnly: false},
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
		        	 var param = params[0];
		        	 var tips="";
		        	 try{
		        		 tips="时间："+param.name.substring(0,param.name.length-3)+ '<br/>光功率值：' + param.value[1]+"dB";
		        		 if(params.length>=2){
		        			 for(var i=1;i<params.length;i++){
		        				 tips+="<br/>"+params[i].seriesName+":"+params[i].value[1]+'dB';
		        			 }
		        		}
		        	 }catch(Exception ){
		        		 tips="";
		        	 }finally{
		        		 return tips;
		        	 }
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
 

//开始时间和结束时间的限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD hh:mm:ss',
    max: laydate.now(), //最大日期
    istime: true,
    festival:true,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas ; //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD hh:mm:ss',
    max: laydate.now(),
    istime: true,
    festival:true,
    choose: function(datas){
        start.max = datas; //结束日选好后，重置开始日的最大日期
    }
};
laydate(start);
laydate(end);


//查询按钮的点击事件
function randomData(powerTime,powerData) {
	    return {
	        name: powerTime,
	        value: [
	            powerTime, 
	            Math.round(powerData*100)/100  //保留两位小数
	        ]
	    }
	}
$("#searchHistoryData").click(function(){
	if(routeID==0){
		  var txt= "请先选择您要查看光功率的光路";
		  var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
				 }
				}
		  window.wxc.xcConfirm(txt, "info", option);
	}
	else if($("#start").val()==""||$("#end").val()==""){
		  var txt= "请选择起始时间";
		  var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
				 }
				}
		  window.wxc.xcConfirm(txt, "info", option);
	}
	else{
		 //光功率实时显示
		   var myChart = echarts.init(document.getElementById("container"));  
		 //从后台获取光功率值
		    var startTime = $("#start").val();
		    var endTime = $("#end").val();
	 		 $.ajax({
	              type : "post",
	              async : false,  //异步请求 先执行后续操作，再执行sucess
	              url : "degradation/checkHisPowerValue",
	              dataType:"json",
	              data : {
	              	     "routeId":routeID,
	              	     "startTime":startTime,
	              	     "endTime":endTime
	              },
	              success:function(json){
	            	  console.log(json.status)
	            	if(json.status){
	            		    var pValues=json.pValues;
	            		    var thresHolds=json.threHolds;
	           		 //将从后台得到的光功率的数据进行一些处理
	           	 		    var data = [];
	           	 		    var thresholdData1 = [];
	           	      	    var thresholdData2 = [];
	           	      	    var thresholdData3 = [];
	           	      	    var thresholdData4 = [];
	           	 		    for(var i = 0;i<pValues.length;i++){
	           	 		    	data.push(randomData(pValues[i].time,pValues[i].pValue));
	           	 		    	if(thresHolds.statusCode){
	           	 		    		thresholdData1.push(randomData(pValues[i].time,thresHolds.thre1));
	           		  		    	thresholdData2.push(randomData(pValues[i].time,thresHolds.thre2));
	           		  		    	thresholdData3.push(randomData(pValues[i].time,thresHolds.thre3));
	           		  		    	thresholdData4.push(randomData(pValues[i].time,thresHolds.thre4));
	           	 		    	}
	           	 		    	
	           	 		    }
	           		   		option = {
	           		   		    title: {
	           		   		        text: '光功率历史数据'
	           		   		    },
	           		   		    toolbox: {
	           		   		        show : true,
	           		   		        feature : {
	           		   		            dataView : {show: true, 
	           		   		            			readOnly: true,
	           		   		            			optionToContent: function(opt) {
	           				                            var axisData = data;//opt.xAxis[0].data;
	           				                            var seriesList = opt.series;
	           				                            var table = '<table style="width:100%;text-align:center"><tbody><tr>'
	           				                                         + '<td>时间</td>'
	           				                                         + '<td>' + seriesList[0].name + '</td>'
	           				                                         + '<td>' + "一级门限" + '</td>'
	           				                                         + '<td>' + "二级门限" + '</td>'
	           				                                         + '<td>' + "三级门限" + '</td>'
	           				                                         + '<td>' + "四级门限" + '</td>'
	           				                                         + '</tr>';
	           				                            for (var i = 0, l = axisData.length; i < l; i++) {
	           				                                table += '<tr>'
	           				                                         + '<td>' + axisData[i].name + '</td>'
	           				                                         + '<td>' + seriesList[0].data[i].value[1] + '</td>'
	           				                                         + '<td>' + seriesList[1].data[i].value[1] + '</td>'
	           				                                         + '<td>' + seriesList[2].data[i].value[1] + '</td>'
	           				                                         + '<td>' + seriesList[3].data[i].value[1] + '</td>'
	           				                                         + '<td>' + seriesList[4].data[i].value[1] + '</td>'
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
	           		   		    tooltip: {
	           		   		        trigger: 'axis',
	           		   		        formatter: function (params) {
	           		   		         var param = params[0];
	           			        	 var tips="";
	           			        	 try{
	           			        		 tips="时间："+param.name.substring(0,param.name.length-3)+ '<br/>光功率值：' + param.value[1]+"dB";
	           			        		 if(params.length>=2){
	           			        			 for(var i=1;i<params.length;i++){
	           			        				 tips+="<br/>"+params[i].seriesName+":"+params[i].value[1]+'dB';
	           			        			 }
	           			        		}
	           			        	 }catch(Exception ){
	           			        		 tips="";
	           			        	 }finally{
	           			        		 return tips;
	           			        	 }
	           		   		        },
	           		   		        axisPointer: {
	           		   		            animation: true
	           		   		        }
	           		   		    },
	           		   		   legend: {
	           		 	           data:['光功率历史数据','阈值一','阈值二','阈值三','阈值四'],
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
	           		   		        name: '光功率历史数据',
	           		   		        type: 'line',
	           		   		        showSymbol: true,
	           		   		        hoverAnimation: true,
	           		   		        data: data
	           		   		    },
	           		   		 {
	           	 	   		        name: '阈值一',
	           	 	   		        type: 'line',
	           	 	   		        showSymbol: false,
	           	 	   		        hoverAnimation: false,
	           	 	   		        data: thresholdData1
	           	 	   		      },
	           	 	   		  {
	           	 	 	   		        name: '阈值二',
	           	 	 	   		        type: 'line',
	           	 	 	   		        showSymbol: false,
	           	 	 	   		        hoverAnimation: false,
	           	 	 	   		        data: thresholdData2
	           	 	 	   		      },
	           	 	   		  {
	           		   		        name: '阈值三',
	           		   		        type: 'line',
	           		   		        showSymbol: false,
	           		   		        hoverAnimation: false,
	           		   		        data: thresholdData3
	           		   		      },
	           	 	   		{
	           		   		        name: '阈值四',
	           		   		        type: 'line',
	           		   		        showSymbol: false,
	           		   		        hoverAnimation: false,
	           		   		        data: thresholdData4
	           		   		      }
	           		   		    ]
	           		   		};
	           		   		myChart.setOption(option);
	            	}
	            	else{
	            		  var txt= "当前光路不存在指定时间范围内的历史光功率数据，请核对后重试。";
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
					    var txt="获取历史光功率失败，";
			  			txt+="失败原因：";
			  			if(XMLHttpRequest.status==401){
			  			    txt+="您不具有当前操作的权限。";
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
	 	
	}
})

//点击光路节点
var routeID = 0;
function clickRoute(routeid){
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	routeID = routeid;
}
$("#startDegra").click(function (){
	if(routeID==0){
		  var txt= "请先选择您要进行劣化分析的光路";
		  var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
				 }
				}
		  window.wxc.xcConfirm(txt, "info", option);
	}
	else
	$(".sidebarDiv").load("html/degradation/degradeParaSetting.html");
})

$("#setThrePara").click(function(){
	if(routeID==0){
		  var txt= "请先选择您要设置劣化参数的光路";
		  var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
				 }
				}
		  window.wxc.xcConfirm(txt, "info", option);
		}
	else
	$(".sidebarDiv").load("html/degradation/setThresholdPara.html");
})


function openSpeficTree(stationId){
	leftTree.config.folderLinks=false;
	leftTree.closeAll();
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	var stationid="2_"+stationId;
	leftTree.openTo(stationid,false);
}