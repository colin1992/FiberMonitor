/**
 * 新增事件
 */
$(document).ready(function(){
	$("#startSite").val((ASite<BSite)?ASite:BSite);
	$("#endSite").val((ASite>BSite)?ASite:BSite);
})

   
/**--------close------------*/
$(".closeEventCon").click(function(){
        $("#eventPara").html("");
        $("#eventPara").hide();
})
    /**--------cancel------------*/
$(".cancel").click(function(){
        $("#eventPara").html("");
        $("#eventPara").hide();
 })
      /**--------confirm------------*/
$(".confirm").click(function(){
        var lossIndex=Math.abs(1000*(AValue-BValue)/(ASite-BSite)).toFixed(3);
        var position=$("#startSite").val();
        var startSite=(AXIndex<BXIndex)?AXIndex:BXIndex;
        var type=$("#type").val();
        $("#eventPara").html("");
        $("#eventPara").hide();
        $.ajax({
        	 url:"../../curve/checkCurve/addEvent",//服务器地址
          	 type:"POST",//采用POST请求
          	 dataType:"json",//否则用Text或其他
          	 timeout:2000,
          	 data:{
          	        'curveId':curveId,//获取SNo
          	        'lossIndex':lossIndex,
          	        'startSite':startSite,
          	        'type':type
          	  },
           success:function(json)
          	  {
           	      if(json[0].status){
           	    	setEventListTable([type,position,"----","----",lossIndex,"----"]);//在事件表中加入刚刚增加的事件
           	    	var txt= "增加成功\n";
           	    	var option = {
            					title: "提示",
            					btn: parseInt("0001",2),
            					onOk: function(){//点击确认的执行方法
            						 var pointObj={};
            				        	  pointObj.name=type;//事件类型
            				        	  pointObj.value=startSite;//位置
            				        	  pointObj.xAxis=otdrCurve[startSite][0];
            				        	  pointObj.yAxis=otdrCurve[startSite][1];
            				        	  markPointsData.push(pointObj);
            				        	  addMarkPoint(markPointsData);
            				        }
           	    	  }
            				
            		  window.wxc.xcConfirm(txt, "info", option);
           	      }
           	      else{
           	    	var txt= "增加失败\n";
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
        })
  })
  /**--------在曲线上画上新加的事件点--------*/
  function addMarkPoint(markPoint){
	  myChart.setOption({ 
        	       series:{ 
	        	    	     name:'OTDR测试曲线',
	        	    	     markPoint:{//
	    		            	   data:markPoint,
	    		            	 },//end markPoint
	        			  },
		                
		            });        
 }