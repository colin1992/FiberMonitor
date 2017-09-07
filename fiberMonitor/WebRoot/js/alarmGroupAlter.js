/**
 * 
 */
/*---------全局变量------------*/
  var groupId=0;
  var CM=0;
  var alarmGroupIdInAlter=[];
  var alarmGroupIdNameAlter=[];
/*----------页面载入时执行----------------*/
$(document).ready(function(){
	 groupId=localStorage.getItem("thisGroupId");
	 CM=$(".obstacleTableDetail tbody tr")[0].children[4].innerHTML;
	 var alarmGroupPara=getAlarmGroupById(groupId);
	 $("#id01").val(alarmGroupPara[0]);//id
	 $("#id02").val(alarmGroupPara[1]);//name
	 $("#id03").val(alarmGroupPara[2]);//description
	
})



/*----------获取告警组信息---------*/
 function  getAlarmGroupById(id){
	var alarmGroupPara=[];
	$.ajax({
		    url:"getAlarmGroupById",
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	                "id":id,  
	              },
		    success: function(json){
			   if(json[0].status){//delete成功
				   alarmGroupPara=[
				                    json[0].id,
				                    json[0].name,
				                    json[0].description
				                   ];
			   }
			}
	})
	return alarmGroupPara;
}




/*--------取消-------*/
$(".paraCancel").click(function(){
	 $("#alarmGroupArea").css("display","none");
	 $(".routeParaSidebar").hide();
})
/*-----------close-------------*/
$(".sidebar_close").click(function(){
	 $("#alarmGroupArea").css("display","none");
	 $(".routeParaSidebar").hide();
})
/*-------------确认----------------*/
$(".paraConfirm").click(function(){
	var alarmGroupPara=[
	                       $("#id01").val(),
	                       $("#id02").val(),
	                       $("#id03").val()
	                   ];
	 $(".alarmAlterArea").html("");
	 $(".alarmAlterArea").hide();
	alterAlarmGroup(alarmGroupPara);
	
	// $(".routeParaSidebar").hide();
})

/*-----------修改告警组-------------*/
function alterAlarmGroup(alarmGroupPara){
	 $.ajax({
	        url:"alterAlarmGroup",
	        dataType:'json', //接受数据格式
	        async: false,
	        data:{
	                "id":alarmGroupPara[0],  
	                "name":alarmGroupPara[1],
	                "description":alarmGroupPara[2]
	              },
		    success: function(json){
			   if(json[0].status){//alter成功
				   $("#alarmGroupArea").css("display","none");
				   getRouteByRtuId(CM);//刷新表格
				   var txt= "修改成功。";
				   var option = {
							title: "提示",
							btn: parseInt("0001",2),
							onOk: function(){//点击确认的执行方法
								var status=getPrioritysInAlter(CM);
								   if(status){   
									  var option='';
									  $("#LevelList").empty();//先清空
								      for(var count=0;count<alarmGroupIdInAlter.length;count++){
								    	  option+="<li><a href='#' id="+alarmGroupIdInAlter[count]+">"+alarmGroupIdNameAlter[count]+"</a></li>";
								       }
								      $("#LevelList").append(option);
								   } 
							}
						}
				  window.wxc.xcConfirm(txt, "info", option);
				}
			   else{
				   var txt= "修改失败\n";
				   txt+="错误原因："+"数据库操作错误";    
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
			  var txt= "修改失败，网络错误\n";
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

/*--------查询数据库，获得当前RTU下所有告警组的id和name----------*/
function getPrioritysInAlter(rtuId){
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
		       alarmGroupIdInAlter=json[0].id;
               alarmGroupIdNameAlter=json[0].name;
               checkStatus=true;
		    }
		},
	   error: function(){
		 
		},
   })
  return checkStatus;
}


