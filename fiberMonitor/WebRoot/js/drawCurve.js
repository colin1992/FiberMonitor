/**
 * 
 */
/*------入口参数为数据点和事件点类型和位置信息---------*/
function drawCurve(curveData,typeAndSite){
      var myChart = echarts.init(document.getElementById('curveDisplayArea'));//获取显示曲线的区域
	  var xdata = [];
      for (var i=0; i<curveData.length; i++){
          xdata.push(i+1);
      }
      var markPoint=[];
      for(var index=0;index<typeAndSite.length;index++){
    	  var pointObj={};
    	  pointObj.name=typeAndSite[index][0];//事件类型
    	  pointObj.value=typeAndSite[index][1];//位置
    	  pointObj.xAxis=typeAndSite[index][1];
    	  pointObj.yAxis=curveData[parseInt(typeAndSite[index][1])];
    	  markPoint.push(pointObj);
      }
     // 指定图表的配置项和数据
     var option={
    		   title: {text: 'OTDR曲线'},
		       tooltip : {//提示框
		                  trigger: 'axis',
		                  triggerOn:'mousemove',
		            	  formatter: function (params) {
		                	  var para=params[0];
		                	  return para.seriesName+
              	     		"<br/>衰减：" +para.data+
            	     		"<br/>与A端距离:"+para.name;
		                }
		          },
		          toolbox: {//工具栏
		    	         show:true,
		    	         left:'center',//将工具栏居中
		                 feature:{
				                 dataZoom:{show:true},//区域缩放
				                 mark:{show:true},
				                 dataView:{show: true,readOnly: true},
				                 magicType:{show: true, type: ['line','bar']},  //图形切换
				                 restore:{show: true},
				                 saveAsImage:{show: true,pixelRatio:3}//保存为图片
			               }
			        },
			      calculable : true,
			      dataZoom: [
			                   {
								    type: 'slider',
								    show: true,
								    realtime : true,
								    start: 0,
								    end: 100,
								    handleSize: 8
								},
								{
								    type: 'inside',
								    realtime : true,
								    start: 0,
								    end: 100
								},
								{
								    type: 'slider',
								    show: true,
								    realtime : true,
								    yAxisIndex: 0,
								    filterMode: 'empty',
								    width: 12,
								    height: '70%',
								    handleSize: 8,
								    showDataShadow: false,
								    left: '93%'
								}
		              ],
		          xAxis: {
		        	    type:'category',
		        	    boundaryGap : true,
					    name:'位置',
					    nameGap:'30',
						nameLocation:'middle',
						splitLine: {show: true},//分割线
		                data: xdata
		          },
		          yAxis: {
		        	     type:'value',
		        	     name:'衰减',
		 			     nameGap:'20',
		 				 nameLocation:'end',
		 				 splitLine: {show: true},//分割线
		 				  axisLabel:{formatter: '{value}dB/km  '}
		          },
		          series: [{
		              name: '详情',
		              type: 'line',
		              data: curveData,
		              itemStyle: {normal:{color:'#aaffff'}
		                         },
		              markPoint : {//
		            	   data:markPoint,
		            	   itemStyle: {
	                                normal: {
	                                    borderColor: '#87cefa',
	                                    label: {//标签显示事件点的顺序
	                                    	    show:true,
						                        formatter: function (params) {
						                        	return params.dataIndex+1;
								                	     		 
						                        },
						                }
					                }
		            	   } 
		               },//end markPoint
		          }]
      };
     // 使用刚指定的配置项和数据显示图表。
     myChart.setOption(option);
    /*---------------以下为事件处理-----------------*/
     
     
     //鼠标点击事件
     myChart.on('click', function (param) {  
    	 document.getElementById("log").innerHTML="组件类型："+param.componentType+"<br/>event类型："+param.type+"<br/>事件点类型:"+ param.value;
     }); 
     myChart.on('mouseover', function (param) {  
    	document.getElementById("log").innerHTML="组件类型："+param.componentType+"<br/>事件类型："+param.type+"<br/>距离A端位置:"+ param.name+"<br/>衰减值:"+param.value+"dB";
    	
     }); 
     myChart.on('mousemove', function (param) {  
        if(param.componentType=="markPoint"){//事件点标记
        	var msg='';
        	for(key in param){
        		msg+=key+":"+param[key]+"&nbsp,&nbsp"
        	}
           	document.getElementById("log").innerHTML=msg;

        	myChart.setOption({
        		tooltip : {//提示框
	                  formatter: function (param) {
	                	  return "事件点序号："+(param.dataIndex+1)+
	                	         "<br/>事件类型："+param.name+
	                	         "<br/>与A端距离:"+param.value;
	                }
	          }
        	});
        }  
       
   });
   myChart.on('mouseout', function (param) {  
        if(param.componentType=="markPoint"){//鼠标离开事件点位置，提示框显示正常点的信息
        		myChart.setOption({
            		tooltip : {//提示框
            			formatter: function (params) {
            	          	  var para=params[0];
            	          	  return para.seriesName+
            		     		"<br/>衰减：" +para.data+
            		     		"<br/>与A端距离:"+para.name;
            	          }
            	        }
    	       });
            	
         }
         });
     
 }