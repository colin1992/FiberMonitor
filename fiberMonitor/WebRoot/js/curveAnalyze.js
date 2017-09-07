/***
 * 
 */
/**-----------全局变量-----------*/
var sample=0;//采样率
var refIndex=0;//折射率
var endSite=0;//结束事件位置
var maxRange=0;
var reMaxRange=0;
var typeAndSite=[];
var themeIndex=0;
var myChart;
var ASite=0;
var BSite=0;//坐标轴上的m
var AXIndex=0;
var BXIndex=0;//在数据中的序号，主要用来增加事件点
var AValue=0;
var BValue=0;
var perPix=0;//横坐标的像素间距
var axisAMove=0;
var axisASite=0;
var curveId;
var refrenceOn=false;//标识位，判断是否已经从数据库中获取到参考曲线
var isRefrenceShow=false;//判断当前页面中是否显示了参考曲线，用户视图切换时恢复显示游标
var markPointsData=[];
var otdrCurve=[];
var refrenceCurve=[];
var max=0;


/**------------页面载入时生成树状图和获取曲线数据--------------*/
// $(document).ready(function(){
	     curveId=localStorage.getItem('curveId');//获取测试曲线
	     if((curveId!=null)&&(curveId!="undefined")){//缓存区的curveId不为空，曲线数据来自点名测试
    	    myChart=echarts.init(document.getElementById('curveDisplayArea'));//获取显示曲线的区域;
      	    getCurvePara(curveId);//获取曲线数据
          }
//	}); 

    /**-----------获取曲线数据，事件点，测试条件等信息------------*/
    function getCurvePara(curveId){
    	$.ajax({
        	   
             url:"../../getCurveAndParas",//服务器地址
           	 type:"post",//采用POST请求
           	 dataType:"json",//否则用Text或其他
           	 timeout:3000,
           	 data:{
           	        'curveId':curveId,//获取SNo
           	  },
            success:function(json)
           	  {
            	getPara(json);
           	  },
           	 error:function(XMLHttpRequest,Error,F)
       	       {
           		 var txt= "网络故障，请检查您的网络连接状况\n";
           		 if(XMLHttpRequest.status==0){
           			 txt="连接超时";
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
       	             
       	         }
       });
    }
    /**-------------设置参数显示区的值---------------*/
    function setTestParaArea(testPara){
    	for(var key in testPara){
    		if(key!="sample"){
    			
    			if(key=='costTime')//将花费时间转换为s,保留一位小数
    			{
    				$('#'+key).text(parseFloat(testPara[key]/1000).toFixed(1));//将时间保留一位小数
    			}
    			else
    			{
    				$('#'+key).text(testPara[key]);//设置显示值
    			}
    		}
    	}
    }
    /**-------------------分析参数-----------------*/
    function getPara(json){
    	otdrCurve=json[0].curve;//曲线数据
    	var events=json[0].events;//事件点
	    var testPara=json[0].testPara//测试条件
	    var testResult=json[0].testResult//测试结果
	    typeAndSite=[];
	    for(var index=0;index<events.length;index++){
	    	if(events[index].type=="结束事件"){
	    		endSite=events[index].site;//结束事件位置
	    		index=events.length;
	    	}
	    }
	    sample=testPara.sample;//采样率
 	    refIndex=testPara.P15;//折射率
 	    maxRange=testPara.P11;
 	    perPix=($('#curveDisplayArea').width()-80)/maxRange; 
 	    /**横坐标的像素间距  domWidth/maxRange 
 	                               坐标轴的宽度为容器宽度减去两边的间距
 	    */
 	    setTestParaArea(testPara);//显示测试条件参数
 	    setTestResult(testResult);//显示测试结果
 	    for(var index=0;index<events.length;index++){
	    	typeAndSite.push([events[index].type,events[index].site]);
	    	try{
	    	var eventPara=[   //事件点参数
	    	                  events[index].type,//类型
	    	                  otdrCurve[events[index].site][0],//位置
	    	                  parseFloat(events[index].insertLoss).toFixed(3),//插入损耗
	    	                  parseFloat(events[index].returnLoss).toFixed(3),//回波损耗
	    	                  parseFloat(events[index].attenIndex).toFixed(3),//衰减系数
	    	                  parseFloat(events[index].totalLoss).toFixed(3)//累计损耗
	    	               ];
	    	if(index==0){  //开始事件的插入损耗、衰减系数、累计损耗无效
	    		eventPara[2]='----';
	    		eventPara[4]='----';
	    		eventPara[5]='----';
	    	}
	    	if( events[index].type=="结束事件"){//结束事件的插入损耗无效
	    		eventPara[2]='----';
	    	}
	    	if(events[index].returnLoss==null)//为用户增的事件
	    	{
	    		eventPara[2]='----';
	    		eventPara[3]='----';
	    		eventPara[5]='----';
	    	}
	    	setEventListTable(eventPara);//事件列表中写入事件点信息
	    }
	    catch(e){
	    	
	    }
 	  }
	    drawCurve(otdrCurve,typeAndSite);//绘制曲线
	    setAxisPointer();
    }
    /**---------设置测试结果--------*/
    function setTestResult(testResult){
    	$('#chainLoss').text(parseFloat(testResult.chainLoss).toFixed(3));//链损耗
    	$('#chainLength').text(parseFloat(testResult.chainLength).toFixed(2));//链长
    	$('#testTime').text(testResult.testTime);//测量时间
    	/**链衰减系数
    	 * 链衰减/链长
    	 * 单位为dB/km  保留三位小数
    	 * */
    	$('#chainLossIndex').text(parseFloat(testResult.chainAtten).toFixed(3));
       	ASite=$("#lineA").css('margin-left');
        if(ASite[ASite.length-1]=='%'){//判断是否是百分比形式
    		ASite=Math.floor((ASite.split("%")[0]/100)*maxRange);//
    	}
    	
    	else{
    		ASite=Math.floor(ASite/$('#axisPointer').width()*maxRange);
    		}
    	for(var index=0;index<otdrCurve.length;index++){
    		if(Math.abs(otdrCurve[index][0]-ASite)<5){
    			AXIndex=index;
    			index=otdrCurve.length
    		}
    	}
     	BSite=$("#lineB").css('margin-left');
        if(BSite[BSite.length-1]=='%'){//判断是否是百分比形式
    		BSite=Math.floor((BSite.split("%")[0]/100)*maxRange);//
    	}
    	
    	else{
    		 BSite=Math.floor(BSite/$('#axisPointer').width()*maxRange);
    		}
    	for(var index=0;index<otdrCurve.length;index++){
    		if(Math.abs(otdrCurve[index][0]-BSite)<5){
    			BXIndex=index;
    			index=otdrCurve.length
    		}
    	}
    	ASite=otdrCurve[AXIndex][0];
    	BSite=otdrCurve[BXIndex][0];//将值换为曲线上的值
    	AValue=otdrCurve[AXIndex][1];//A衰减值
    	BValue=otdrCurve[BXIndex][1];//B衰减值
    	$("#AorB").text('B点衰减：');//将label改为A
  	    $("#attenValue").text(BValue);
  	    $('#ABGraps').text(Math.abs((ASite-BSite).toFixed(3)));
      	$('#ABAtten').text(Math.abs((AValue-BValue).toFixed(3)));
      	$('#ABAttenIndex').text(Math.abs(1000*(AValue-BValue)/(ASite-BSite)).toFixed(3));
    }
   
    /**------绘制曲线
     * 
     * 入口参数为数据点和事件点类型和位置信息---------*/
    function drawCurve(otdrCurve,typeAndSite){
    	  markPointsData=[];
          for(var index=0;index<typeAndSite.length;index++){
        	  var pointObj={};
        	  pointObj.name=typeAndSite[index][0];//事件类型
        	  pointObj.value=parseInt(typeAndSite[index][1]);//位置
        	  pointObj.xAxis=otdrCurve[typeAndSite[index][1]][0];//获取x值
        	  pointObj.yAxis=otdrCurve[parseInt(typeAndSite[index][1])][1];//获取y值
        	  markPointsData.push(pointObj);
          }
         // 指定图表的配置项和数据
         var option={
        		   title: {text: 'OTDR曲线'},
        		    grid:{
        		        x:40,
        		        y:60,
        		        x2:40,
        		        y2:60
        		      },
    		         tooltip:{//提示框
    		    	          show:false,
    		    	          trigger: 'axis',
    		                  triggerOn:'mousemove',
    		                  axisPointer : {  
                     		  type : 'line',  
   							  lineStyle : {  
   									color : 'rgba(252,252,252,0)',  
   									width : 2,  
   									type : 'solid'  
                      			  }
   							   },
   							
    		          },
    		          toolbox: {//工具栏
    		    	         show:true,
    		    	         orient:'horizontal',
    		    	         right:'center',
    		    	         top:'10px',
    		    	         feature:{
    				                 dataZoom:{show:true},//区域缩放
    				                 mark:{show:true},
    				                 dataView:{show: true,readOnly: true,
    				                	 optionToContent: function(opt) {
    				                		    $("#lineA").hide();
    				                		    $("#lineB").hide();
    				                		    var table = '<table style="width:50%;text-align:center"><tbody><tr>'
    				                		                 + '<td style="width:10%">位置[m]</td>'
    				                		                 + '<td style="width:10%">' +"测试曲线衰减[dB]"+ '</td>'
    				                		                 + '</tr>';
    				                		    for (var i=0;i<otdrCurve.length;i++) {
    				                		        table += '<tr>'
    				                		                 + '<td>'+otdrCurve[i][0]+'</td>'
    				                		                 + '<td>'+otdrCurve[i][1]+'</td>'
    				                		                 + '</tr>';
    				                		    }
    				                		    table += '</tbody></table>';
    				                		    return table;
    				                		}
    				                 },
    				                 magicType:{show: true, type: ['line','bar']},  //图形切换
    				                 restore:{ 
    				                	        show: true,
    				                	        title:'全局显示'
    				                          },
    				                 saveAsImage:{show: true,pixelRatio:3},//保存为图片
    				                 mySwitchTool: {//自定义工具，用于切换主题
    				                     show: true,
    				                     title: '主题切换',
    				                     icon: 'image://../../images/magicPen.png',
    				                     onclick: function (){
    				                         themeIndex++;
				                             if(themeIndex==4){
				                            	 themeIndex=0; 
				                             }
    				                         myChart.setOption(
    				                        	theme[themeIndex]
    				                         );
    				                     }
    				                 }
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
    								    show: false,
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
    		        	    type:'value',
    		        	    max:maxRange,
    		        	    min:0,
    					    name:'位置',
    					    nameGap:'30',
    						nameLocation:'middle',
    						splitLine: {show: true},//分割线
    		                axisLabel:{formatter: '{value}m'}//单位：m
    		          },
    		          yAxis: {
    		        	     type:'value',
    		        	     name:'衰减',
    		 			     nameGap:'20',
    		 				 nameLocation:'end',
    		 				 splitLine: {show: true},//分割线
    		 				 axisLabel:{formatter: '{value}dB'}
    		          },
    		          series:[{
    		        	  smooth:true,
    		        	  symbolSize:'1',
    		              name:'OTDR测试曲线',
    		              type:'line',
    		              data: otdrCurve,
    		              itemStyle:{normal:{color:'#2828ff'}
    		                         },
    		              markPoint:{//
    		            	   data:markPointsData,
    		            	   symbolSize:[25,25],
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
    		          }],
    		          animation:true,
    		          animationThreshold:7000,
    		          animationDuration:1000,
    		          animationEasing: 'cubicIn',
    		          progressiveThreshold:2000
          };
       // 使用刚指定的配置项和数据显示图表。
         myChart.setOption(option);
        /**---------------以下为事件处理-----------------*/
         //鼠标点击事件
         myChart.on('click', function (param) { 
        	 if(param.componentType=="markPoint"){//事件点标记
        	   var trs=document.querySelectorAll("#eventListTable tbody tr");//点击事件点时事件点列表的当前项颜色标注
         	   $(trs[param.dataIndex]).addClass("eventOnCurrtr").siblings().removeClass("eventOnCurrtr").removeClass("eventCurrtr");
        	 }
        }); 
        myChart.on('mousemove', function (param) {  
        	 if(param.componentType=="markPoint"){//事件点标记
            	var trs=document.querySelectorAll("#eventListTable tbody tr");//移动到事件点时事件点列表的当前项颜色标注
            	$(trs[param.dataIndex]).addClass("eventOnCurrtr").siblings().removeClass("eventOnCurrtr").removeClass("eventCurrtr");
            	myChart.setOption({
            		tooltip : {//提示框
            			  show:true,
            			  position:null,//采用默认位置，跟踪鼠标
        				  formatter: function (param) {
    	                	  $('#attenValue').text(otdrCurve[parseInt(param.value)][1]);
    	                	  var attIndex=(1000*otdrCurve[parseInt(param.value)][1]/otdrCurve[parseInt(param.value)][0]).toFixed(3);
    	                	  if(attIndex<0){
    	                		  attIndex=attIndex*(-1);
    	                	  }
    	                	  $('#site').text(otdrCurve[parseInt(param.value)][0]);
		                 	  $('#attenIndex').text(attIndex);
    	                	  return "事件点序号："+(param.dataIndex+1)+
    	                	         "<br/>事件类型："+param.name+
    	                	         '<br/>衰减:'+otdrCurve[parseInt(param.value)][1]+'dB'+
    	                	         "<br/>与起点距离:"+otdrCurve[parseInt(param.value)][0]+'m';
    	                }
    	          }
            	});
            }  
           
       });
       myChart.on('mouseout', function (param) {  
            if(param.componentType=="markPoint"){//鼠标离开事件点位置
            	  if(!isRefrenceShow)//未打开参考曲线，关闭游标
            	  {
            		  myChart.setOption({
                  	  tooltip : {
                  		           show:false,
                  		        }
          	       });
            	  }
            	  else{
            		//提示框
            		  myChart.setOption({
                      	  tooltip:{
                      		show:true,
      	    	            axisPointer : {  
                   			   lineStyle : {  
      								color : 'red',  
      							}
      					 },
      					 formatter: function (params) {
      						 if(params.length==2){
      							 var site=params[0].data[0];
      							 var curveLoss=params[0].data[1]+'dB';
      							 var reLoss=params[1].data[1];
      							 if(maxRange<reMaxRange){
      								 site=params[1].data[0];
      							 }
      							 if(site>maxRange){
      								 curveLoss="----";
      							 }
      							 if(curveLoss>reMaxRange){
      								 reLoss="----";
      							 }
      							 var showContext="位置："+site+'m'+
      	          		     		"<br/>曲线衰减：" +curveLoss
      							    +"<br/>参考曲线衰减:"+reLoss+'dB';
      						 }
      						 else{
      							 var showContext="位置："+params[0].data[0]+'m'+
      	          		     		"<br/>曲线衰减："+params[0].data[1]+'dB';
      							    
      						 }
      						 return showContext;
      	          	  }
                      	  }
              	       });
	    	   }
            }
         });
      }   
    /**-----------------------画游标----------------------*/
    function setAxisPointer(){
    	$("#lineA").show(); 
    	$("#lineB").show(); 
    	var axisPointerA = new Draggabilly('#lineA', {
        	axis:'x',
        	containment:true//document.getElementById("showArea")
        });
        var axisPointerB = new Draggabilly('#lineB', {
        	axis:'x',
        	containment:document.getElementById("axisPointer")
        });
         /** un-bind event listener
          * 采用dragStart 而不是dragMove的方式监听事件是为了防止卡顿
          * */
        
        axisPointerA.on('dragStart',axisPointerAMove);
        axisPointerA.on('dragEnd',axisPointerAEnd);
        axisPointerB.on('dragEnd',axisPointerBEnd);
        axisPointerB.on('dragStart',axisPointerBMove);
        
    }
   
    /**-----------------移动游标---------------------*/
    
    /**------------游标A移动-----------*/
    function axisPointerAMove(){
    	axisASite=$("#lineA").offset().left;
    	myChart.setOption({
       		 tooltip : {//提示框
       			show:true,
       			alwaysShowContent:true,
       			position: function (point, params, dom) {
				      // 固定在顶部
				      return [point[0], '10%'];
				  },
    			formatter: function (params) {
    			  var para=params[0];
  	          	  AXIndex=para.dataIndex
            	  AValue=para.data[1];//实时刷新游标A的值
      	          ASite=para.data[0];
            	  $("#AorB").text('B点衰减：');//将label改为A
            	  $("#attenValue").text(BValue);
            	  $('#ABGraps').text(Math.abs((ASite-BSite).toFixed(3)));
  	          	  $('#ABAtten').text(Math.abs((AValue-BValue).toFixed(3)));
  	          	  $('#ABAttenIndex').text(Math.abs(1000*(AValue-BValue)/(ASite-BSite)).toFixed(3));
  	             // $('#chainLength').text(attIndex);
//  	          	console.log("游标A"+
//  	  		     		"<br/>衰减："+para.data[1]+'dB'+
//  	  		     		"<br/>与起点距离:"+para.data[0]+'m');
  	          	  return  "游标A"+
  		     		"<br/>衰减："+para.data[1]+'dB'+
  		     		"<br/>与起点距离:"+para.data[0]+'m';
  	          }
  	        }
       	});
       } 
    /**----------------------游标A end-----------------*/
    function axisPointerAEnd (){
    	//console.log("lineA end move");
    	axisAMove=axisAMove+axisASite-$("#lineA").offset().left;//记录累计移动的位置  
    	myChart.setOption({
       		tooltip :  {//不显示提示框
       			  show:false,
    			}
       	});
    }
   
      /**-----------游标B移动------------*/
     function axisPointerBMove (){
    	myChart.setOption({
       		tooltip :  {//提示框
       			show:true,
       			alwaysShowContent:true,
       			position: function (point, params, dom) {
				      // 固定在顶部
				      return [point[0], '12%'];
				  },
    	     	  formatter: function (params) {
    	          var para=params[0];
    	          BXIndex=para.dataIndex;//游标B的序号
    	          BValue=para.data[1];//实时刷新游标A的值
        	      BSite=para.data[0];
              	  $("#AorB").text('A点衰减：');//将label改为A
              	  $("#attenValue").text(AValue);
           	      $('#ABGraps').text(Math.abs((ASite-BSite).toFixed(3)));
 	          	  $('#ABAtten').text(Math.abs((AValue-BValue).toFixed(3)));
 	          	  $('#ABAttenIndex').text(Math.abs(1000*(AValue-BValue)/(ASite-BSite)).toFixed(3));
              	  return "游标B"+
    		     		"<br/>衰减：" +para.data[1]+'dB'+
    		     		"<br/>与起点距离:"+para.data[0]+'m';
    	          }
    	        }
       	})
     }
      /**----------------------游标B end-----------------*/
      function axisPointerBEnd (){
    	myChart.setOption({
         		tooltip :  {//不显示提示框
         			show:false,
      			}
         	});
      }
      
    /**--------------------------点击表格-----------------------------*/
    $(".eventListTable>tbody tr").click(function() {
        $(this).addClass("eventCurrtr").siblings().removeClass("eventCurrtr").removeClass("eventOnCurrtr");
        var eventIndex=parseInt(this.children[0].innerHTML)-1;//当前事件点序号
        ASite=otdrCurve[typeAndSite[eventIndex][1]][0];
        AValue=otdrCurve[typeAndSite[eventIndex][1]][1];
        $("#AorB").text('A点衰减：');//将label改为A
    	$("#attenValue").text(AValue);
 	    $('#ABGraps').text(Math.abs((ASite-BSite).toFixed(3)));
     	$('#ABAtten').text(Math.abs((AValue-BValue).toFixed(3)));
     	$('#ABAttenIndex').text(Math.abs(1000*(AValue-BValue)/(ASite-BSite)).toFixed(3));
       //容器宽度-两边距离   得到坐标系的宽度
        var x=(otdrCurve[typeAndSite[eventIndex][1]][0]*perPix).toFixed(1);
        /**-----由于lineA在移动过程中由Draggabilly增加了新的样式类，存在偏移量
         * 从而在定位的时候要扣除偏移量的影响------*/
        $('#lineA').css('margin-left',x-axisAMove*(-1)+'px');
        
 });
    
 /**-------------写入事件列表------------*/   
    function setEventListTable(tableData) {      
    	var trs=document.querySelectorAll("#eventListTable tbody tr");
        for(var i=0; i<trs.length; i++){
      	        var Ele = trs[i].children;
      	        if(!Ele[0].innerHTML){
      	        	 Ele[0].innerHTML=i+1;
      	        	for (var j=0;j<tableData.length; j++){
      	                Ele[j+1].innerHTML=tableData[j];
      	               
      	            }
      	        	i=trs.length;
      	        }
      	}
    }
/**-------设为参考曲线按键--------*/   
   $("#saveAsRefrence").click(function(){
	   var txt= "是否将当前曲线设为参考曲线，该操作会替换原来的参考曲线\n";
 	   txt+="确认继续请点击\"确认键\"";
		  var option = {
					title: "提示",
					btn: parseInt("0011",2),
					onOk: function(){//点击确认的执行方法
						saveAsRefrence(curveId);
				 }
				}
		  window.wxc.xcConfirm(txt, "info", option);
   })
   /**------------设为参考曲线-----------*/
   function saveAsRefrence(curveId){
	   $.ajax({
    	   
             url:"../../curve/checkCurve/setRefrence",//服务器地址
          	 type:"POST",//采用POST请求
          	 dataType:"json",//否则用Text或其他
          	 timeout:2000,
          	 data:{
          	        'id':curveId,//
          	  },
           success:function(json)
          	  {
          		if(json[0].status){
          		   var txt= "您已成功将当前曲线设置为参考曲线！";
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
       			    var option = {
       						title: "提示",
       						btn: parseInt("0001",2),
       						onOk: function(){//点击确认的执行方法
       						}
       					}
       			   window.wxc.xcConfirm(txt, "info", option);
          		}
       	   
       	      },
          	 error:function(XMLHttpRequest,Error,F)
      	       {
          		var txt="修改失败<br/>";
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
   
   /**------------------打开/关闭参考曲线按键-------------------*/
   $("#openRefrence").click(function(){
	   if($(this).text()=="打开参考曲线"){
		   if(refrenceOn){
			   isRefrenceShow=true;//参考曲线显示标志位置位
			   $(this).text("关闭参考曲线");
			   $("#lineA").hide();
			   $("#lineB").hide();
			   myChart.setOption({
	               tooltip:{
	        	    	show:true,
	        	    	axisPointer : {  
                 			 lineStyle : {  
									color : 'red',  
							 }
						 },
	        	    	formatter: function (params) {
	        	    		 var showContext="位置："+params[0].data[1]+'m'+
	          		     		"<br/>曲线衰减：" +params[0].data[0]+'dB';
							 if(params.length==2){
								 showContext+="<br/>参考曲线衰减:"+params[1].data[0]+'dB';
							 }
		          	         return showContext;
	          	          }
	        	    },
	        	    toolbox: {//工具栏
	        	    	 orient:'vertical',
	              		 right:'6px',
	              		 top:'5px',
		    	         feature:{
				                dataView:{
				                	 optionToContent: function(opt) {
				                		 var table = '<table style="width:70%;text-align:center"><tbody><tr>'
	                		                 + '<td style="width:10%">测试曲线位置[m]</td>'
	                		                 + '<td style="width:15%">'+"测试曲线衰减[dB]"+ '</td>'
	                		                 + '<td style="width:10%">'+" "+ '</td>'
	                		                 + '<td style="width:10%">参考曲线位置[m]</td>'
	                		                 +'<td style="width:15%">参考曲线衰减[dB]</td>'
	                		                 + '</tr>';
			                		     for (var i=0;i<max;i++) {
			                		    	var curveSite="";
			                		    	var curveValue="";
			                		    	var refSite="";
			                		    	var refValue="";
			                		    	if(i<otdrCurve.length)
			                		    	{
			                		    		curveSite=otdrCurve[i][0];
			                		    		curveValue=otdrCurve[i][1];
			                		    	}
			                		    	if(i<refrenceCurve.length)
			                		    	{
			                		    		refSite=refrenceCurve[i][0];
			                		    		refValue=refrenceCurve[i][1];
			                		    		
			                		    	}
			                		    	table += '<tr>'
		                		                 + '<td>'+curveSite+'</td>'
		                		                 + '<td>'+curveSite+'</td>'
		                		                 + '<td>'+""+'</td>'
		                		                 + '<td>'+refSite+'</td>'
		                		                 + '<td>'+refValue+'</td>'
		                		                 + '</tr>';  
			                		    }
			                		    table += '</tbody></table>';
			                		   // console.log("table:"+table)
			                		    return table;
		    				                
				                		}
				                 }
		    	        }
	        	    },
	        	    xAxis: 
					{
  		        	    max:(reMaxRange>maxRange)?reMaxRange:maxRange,
  		        	},
	        	    legend: {
	                	data:['OTDR测试曲线','参考曲线'],
	                	selected:{
	                		'OTDR测试曲线':true,
	                		'参考曲线':true,
	                	},
	                	show:true,
	        	    },
		      });
		 }
		   else{
			   getRefrence(curveId);//获取参考曲线
		   }
		   
	   }
	   else{//取消显示参考曲线
		   isRefrenceShow=false;
		   $(this).text("打开参考曲线");
		   for(var index=1;index<=7;index++){
		    	var tId="#exTH"+index;
		    	$(tId).text("");
		    }
		    $("#refrenceLabel").hide();//不显示参考曲线列表label
		    $("#lineA").show();
		    $("#lineB").show();
		    myChart.setOption( {
                legend: {
                	selected:{'参考曲线':false},
                	show:false,
        	    },
        	    tooltip:{
        	    	show:false,
        	    	  axisPointer : {  
               			    lineStyle : {  
      							color : 'rgba(252,252,252,0)',  
      						}
      				 },
        	    },
        	   toolbox: {//工具栏  关闭参考曲线后将工具栏还原为水平显示
        		     orient:'horizontal',
	    	         right:'center',
	    	         top:'10px',
	    	         feature:{
			                dataView:{
			                	 optionToContent: function(opt) {
			                		    $("#lineA").hide();
			                		    $("#lineB").hide();
			                		    var table = '<table style="width:40%;text-align:center"><tbody><tr>'
			                		                 + '<td style="width:10%">位置[m]</td>'
			                		                 + '<td style="width:10%">' +"测试曲线衰减[dB]"+ '</td>'
			                		                 + '</tr>';
			                		    for (var i=0;i<otdrCurve.length;i++) {
			                		        table += '<tr>'
			                		                 + '<td>' +otdrCurve[i][0]+'</td>'
			                		                 + '<td>' +otdrCurve[i][1] +'</td>'
			                		                 + '</tr>';
			                		    }
			                		    table += '</tbody></table>';
			                		    return table;
			                		}
			                 }
	    	        }
        	    },
        	   xAxis: {
        		         max:maxRange
        		      },
	               series:
						{    
						 name:'OTDR测试曲线',
						},
		 });
		  clearRefrenceEventTable();//清空参考事件表格
	   }
   })
   /**----------------获取参考曲线------------------*/
   function getRefrence(curveId){
	   $.ajax({
    	   
             url:"../../getReferenceCurve",//服务器地址
        	 type:"POST",//采用POST请求
        	 dataType:"json",//否则用Text或其他
        	 timeout:2000,
        	 data:{
        	        'id':curveId,//
        	  },
         success:function(json)
        	  {
        	    if(parseInt(json[0].status)==0){//不存在参考曲线
        		   var txt= "当前光路不存在参考曲线"+
        		             "您可以点击\"设为参考曲线\"将当前测试曲线设为参考曲线！";
    			   var option = {
    						title: "提示",
    						btn: parseInt("0001",2),
    						onOk: function(){//点击确认的执行方法
    						}
    					}
    			  window.wxc.xcConfirm(txt, "info", option);
        		}
        		if(parseInt(json[0].status)==1){//当前曲线即为参考曲线
        			   var txt= "您当前查看的曲线即为参考曲线";
					   var option = {
								title: "提示",
								btn: parseInt("0001",2),
								onOk: function(){//点击确认的执行方法
								}
							}
					  window.wxc.xcConfirm(txt, "info", option);
        		}
        		else{//有参考曲线且不为当前曲线
        			//console.log("存在参考曲线")
        			 refrenceOn=true;
        			 isRefrenceShow=true;//参考曲线显示标志位置位
        			$("#openRefrence").text("关闭参考曲线");
        			getRefrencePara(json);
        		}
     	   
     	      },
        	 error:function(XMLHttpRequest,Error,F)
    	       {
        		  var txt= "打开失败，网络错误\n";
			      txt+="状态码："+XMLHttpRequest.status+"\n";
			      txt+="错误原因：" +XMLHttpRequest.statusText;    
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
   /**-------------------提取参考曲线参数------------------*/
   function getRefrencePara(json){
	    refrenceCurve=json[0].curve;//参考曲线数据
	    var refrenceEvents=json[0].events;//参考事件点
	    var refrenceTypeAndSite=[];
	    var refrenceEndSite=refrenceEvents[refrenceEvents.length-1].site;//结束事件位置
	    var refrenceSample=json[0].testPara.sample;//采样率
	    var refrenceRefIndex=json[0].testPara.P15;//折射率
	    reMaxRange=json[0].testPara.P11;
	    var refrencePerPix=($('#curveDisplayArea').width()-80)/reMaxRange; 
	    /**横坐标的像素间距  domWidth/maxRange 
	                               坐标轴的宽度为容器宽度减去两边的间距
	    */
	    /**----------设定表格栏名称----------*/
	    var titleNames=[
	                     "序号",
	                     "事件类型",
	                     "位置[m]",
	                     "插入损耗[dB]",
	                     "回波损耗[dB]",
	                     "衰减系数[dB/km]",
	                     "累计损耗[dB]"
	                   ];
	    for(var index=1;index<=titleNames.length;index++){
	    	var tId="#exTH"+index;
	    	$(tId).text(titleNames[index-1]);
	    }
	    $("#refrenceLabel").show();//显示参考曲线列表label
	    /**-----------折射率----------采样率----------perPix-----*/
	   for(var index=0;index<refrenceEvents.length;index++){
	    	refrenceTypeAndSite.push([refrenceEvents[index].type,refrenceEvents[index].site]);
	    	try{
		    	var refrenceEventPara=[   //事件点参数
		    	                  refrenceEvents[index].type,//类型
		    	                  refrenceCurve[refrenceEvents[index].site][0],//位置
		    	                  Math.floor(refrenceEvents[index].insertLoss* 1000)/1000,//插入损耗
		    	                  Math.floor(refrenceEvents[index].returnLoss* 1000)/1000,//回波损耗
		    	                  Math.floor(refrenceEvents[index].attenIndex* 1000)/1000,//衰减系数
		    	                  Math.floor(refrenceEvents[index].totalLoss* 1000)/1000//累计损耗
		    	               ];
		    	if(index==0){  //开始事件的插入损耗、衰减系数、累计损耗无效
		    		refrenceEventPara[2]='----';
		    		refrenceEventPara[4]='----';
		    		refrenceEventPara[5]='----';
		    	}
		    	if(refrenceEvents[index].type=="结束事件"){//结束事件的插入损耗无效
		    		refrenceEventPara[2]='----';
		    	}
		    	if(refrenceEvents[index].returnLoss==null)//为用户增的事件
		    	{
		    		refrenceEventPara[2]='----';
		    		refrenceEventPara[3]='----';
		    		refrenceEventPara[5]='----';
		    	}
		    	
		     setRefrenceEventTable(refrenceEventPara);//事件列表中写入事件点信息
	    	}catch(e){
	    		
	    	}
		    }
	   drawRefrenceCurve(refrenceCurve,refrenceTypeAndSite);//画参考曲线
   }
   /**------------------在表格中填充参考事件-------------------*/
     function setRefrenceEventTable(tableData){
    	 	var tr=document.querySelectorAll("#eventListTable tbody tr");
    	    for(var i=0; i<tr.length; i++){
          	        var Ele=tr[i].children;
          	        if(!Ele[8].innerHTML){//参考事件从第八列开始,从空白列开始填充
          	        	Ele[8].innerHTML=i+1;
          	            for (var j=0;j<tableData.length;j++){
          	                Ele[j+9].innerHTML=tableData[j];  
          	               
          	            }
          	            i=tr.length;//一次只填充一行，所以执行一次直接使i到上限
          	        }
          	}
     } 
     /**--------------------清空参考曲线事件列表显示---------------------*/
     function clearRefrenceEventTable(){
 	    var trs=document.querySelectorAll("#eventListTable tbody tr");//全部的行
         for(var i=0; i<trs.length; i++){
       	        var Ele = trs[i].children;
       	        if(Ele[8].innerHTML){//参考事件从第八列开始,不为空的清空
       	        	for (var j=8;j<Ele.length;j++){
       	                Ele[j].innerHTML='';
       	            }
       	        }
       	 }
  } 
     /**----------------画参考曲线-----*/
     
   function drawRefrenceCurve(refrenceCurve,refrenceTypeAndSite){
	  // console.log("参考曲线数据："+refrenceCurve)
	   	var markPoints=[];
        for(var index=0;index<refrenceTypeAndSite.length;index++){
      	  var pointObj={};
      	  pointObj.name=refrenceTypeAndSite[index][0];//事件类型
      	  pointObj.value=refrenceTypeAndSite[index][1];//位置
      	  pointObj.xAxis=refrenceCurve[parseInt(refrenceTypeAndSite[index][1])][0];//x坐标值
      	  pointObj.yAxis=refrenceCurve[parseInt(refrenceTypeAndSite[index][1])][1];//y坐标值
      	   markPoints.push(pointObj);
        }
        $("#lineA").hide();
        $("#lineB").hide();
        max=(maxRange>reMaxRange)?maxRange:reMaxRange;
       var series=
			        {
					  tooltip:{//提示框
		    	          show:true,
		    	          axisPointer : {  
                 			   lineStyle : {  
									color : 'red',  
								}
						 },
						 formatter: function (params) {
							 if(params.length==2){
								 var site=params[0].data[0];
								 var curveLoss=params[0].data[1]+'dB';
								 var reLoss=params[1].data[1];
								 if(maxRange<reMaxRange){
									 site=params[1].data[0];
								 }
								 if(site>maxRange){
									 curveLoss="----";
								 }
								 if(curveLoss>reMaxRange){
									 reLoss="----";
								 }
								 var showContext="位置："+site+'m'+
		          		     		"<br/>曲线衰减：" +curveLoss
								    +"<br/>参考曲线衰减:"+reLoss+'dB';
							 }
							 else{
								 var showContext="位置："+params[0].data[0]+'m'+
		          		     		"<br/>曲线衰减："+params[0].data[1]+'dB';
								    
							 }
							 return showContext;
		          	  }
							
		              },
	              	  toolbox: {//工具栏  显示参考曲线的时候由于水平位置要显示图例，古将工具栏放在右侧
	              		 orient:'vertical',
	              		 right:'10px',
	              		 top:'10px',
 		    	         feature:{
 				                  dataView:{
 				                	 optionToContent: function(opt) {
 				                		   $("#lineA").hide();
 				                		   $("#lineB").hide();
 				                		   max=(otdrCurve.length>refrenceCurve.length)?otdrCurve.length:refrenceCurve.length;
 				                		   var table = '<table style="width:70%;text-align:center"><tbody><tr>'
		                		                 + '<td style="width:10%">测试曲线位置[m]</td>'
		                		                 + '<td style="width:10%">'+"测试曲线衰减[dB]"+ '</td>'
		                		                 + '<td style="width:10%">'+" "+ '</td>'
		                		                 + '<td style="width:10%">参考曲线位置[m]</td>'
		                		                 +'<td style="width:10%">参考曲线衰减[dB]</td>'
		                		                 + '</tr>';
 				                		  for (var i=0;i<max;i++) {
				                		    	var curveSite="";
				                		    	var curveValue="";
				                		    	var refSite="";
				                		    	var refValue="";
				                		    	if(i<otdrCurve.length)
				                		    	{   
				                		    		curveSite=otdrCurve[i][0];
				                		    		curveValue=otdrCurve[i][1];
				                		    	}
				                		    	
				                		    	if(i<refrenceCurve.length)
				                		    	{
				                		    		refSite=refrenceCurve[i][0];
				                		    		refValue=refrenceCurve[i][1];
				                		    		
				                		    	}
				                		    	table += '<tr>'
			                		                 + '<td>'+curveSite+'</td>'
			                		                 + '<td>'+curveValue+'</td>'
			                		                 + '<td>'+" "+'</td>'
			                		                 + '<td>'+refSite+'</td>'
			                		                 + '<td>'+refValue+'</td>'
			                		                 + '</tr>';  
				                		    	
				                		    }
				                		    table += '</tbody></table>';
				                		    return table;
 				                		}
 				                 }
 				              }
	              	       },
	              	   xAxis: {
	              		   		max:max,
	              	   	},
	    		       series:[
								{
								  name:'OTDR测试曲线',
								  },
  		                        {  
			  		              name:'参考曲线',  
			  		              type:'line',
			  		              data:refrenceCurve,
			  		              symbolSize:'1',
			  		              itemStyle:{normal:{color:'black'}
			  		                         },
			  		              markPoint:{//
			  		            	   data:markPoints,
			  		            	   symbolSize:[25,25],
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
  		               	}
                      ],
  		              legend:{
		         	      show:true,
		         	      top:'10px',
		         	      left:'center',
		         		  data:['OTDR测试曲线','参考曲线'],
		               	  selected:{
		               		'OTDR测试曲线':true,
		               		'参考曲线':true,
		               	},
		               	
		       	    },
	               
			     };
   myChart.setOption(series);
 }
   /**----------------增加事件点按键--------------*/
   $("#addEvent").click(function(){
	   if(isRefrenceShow){
		   var txt= "操作错误！\n"
			   +"错误提示：不能在参考曲线打开的情况下增加事件点\n"
			   +"请先点击\"关闭参考曲线\"按钮，待取消显示参考曲线后重试！";
  	    	var option = {
   					title: "提示",
   					btn: parseInt("0001",2),
   					onOk: function(){//点击确认的执行方法
   				  }
   				}
   		  window.wxc.xcConfirm(txt, "info", option);
	   }
	   else{
		   $("#eventPara").show();
		   $("#eventPara").load("../../html/curve/addEvent.html")
	   }
	   
   })

  
 
   
   
   
 