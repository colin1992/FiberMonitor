/**
 * 
 */
/**
 * 
 */
/*---------全局变量------------*/
  var CM=0;
  var alarmIdInAdd=[];
  var alarmNameInAdd=[];
/**----------页面载入时执行----------------*/
$(document).ready(function(){
	 CM=$(".obstacleTableDetail tbody tr")[0].children[4].innerHTML;
	 $("#id01").val(CM);//RTU id	
})

/**--------取消-------*/
$(".paraCancel").click(function(){
	 $("#alarmGroupArea").css("display","none");
	 $(".routeParaSidebar").hide();
})
/**-----------close-------------*/
$(".sidebar_close").click(function(){
	 $("#alarmGroupArea").css("display","none");
	 $(".routeParaSidebar").hide();
})
/**-------------确认----------------*/
$(".paraConfirm").click(function(){
	var alarmGroupPara=[
	                       $("#id01").val(),
	                       $("#id02").val(),
	                       $("#id03").val()
	                   ];
	 $(".alarmAlterArea").html("");
	 $(".alarmAlterArea").hide();
	 addAlarmGroup(alarmGroupPara);

})

/**-----------增加告警组-------------*/
function addAlarmGroup(alarmGroupPara){
	 $.ajax({
	        url:"addAlarmGroup",
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	                "CM":CM,  
	                "name":alarmGroupPara[1],
	                "description":alarmGroupPara[2]
	              },
		    success: function(json){
			   if(json[0].status){//add成功
				   $("#alarmGroupArea").css("display","none");
				   var txt= "增加成功.";
				   var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								var status=getPrioritysInAdd(CM);
								   if(status){   
									  var option='';
									  $("#LevelList").empty();//先清空
								      for(var count=0;count<alarmIdInAdd.length;count++){
								    	  option+="<li><a href='#' id="+alarmIdInAdd[count]+">"+alarmNameInAdd[count]+"</a></li>";
								       }
								      $("#LevelList").append(option);
								   } 
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
				}
			   else{
				   var txt= "增加失败,";
				   txt+="失败原因："+"数据库操作错误";    
			       var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
			  }
		  },
		  error:function(XMLHttpRequest,textStatus,F){
			  var txt= "增加失败，网络错误。";
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
	 })
}

/**--------查询数据库，获得当前RTU下所有告警组的id和name----------*/
function getPrioritysInAdd(rtuId){
   var checkStatus=false;
   $.ajax({
        url:"getPriorityLevel",
        dataType:'json', //接受数据格式
        async: false,
        data:{
                "rtuId":rtuId,   
              },
	    success: function(json){
		   if(json[0].status){//查询成功
		       alarmIdInAdd=json[0].id;
               alarmNameInAdd=json[0].name;
               checkStatus=true;
		    }
		},
	   error: function(){
		 
		},
   })
  return checkStatus;
}

