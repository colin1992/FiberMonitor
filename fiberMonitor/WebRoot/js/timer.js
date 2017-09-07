/**
 * 
 */
var minute=0;
var second=0;//s
var tenMs=0;//10ms
var waitAlert="您已等待：";
 
timerForCallTest=setInterval("timedCount()",10);//10ms执行一次 
function timedCount(){    
	 showTime();
	 if(tenMs==99){
		 tenMs=0;
	 	if(second==59){
				second=0;
                minute+=1;
			}
		 second+=1;
	 }
	 tenMs+=1;
 }
  
 function showTime(){
	 
	 var showM = (minute < 10 ? "0" + minute: minute); //格式化分钟
     var showS = (second < 10 ? "0" + second : second); //数格式化秒数
	 var showMs = (tenMs < 10 ? "0" + tenMs : tenMs); //数格式化秒数
	 var showTime=waitAlert+showM+':'+showS+':'+showMs;
	 try{
		 document.getElementById("showTime").innerHTML=showTime;	
	 }catch(e){
		 
	 }	
 } 
  
function stopCount(){
	clearInterval(timerForCallTest);
    minute=0;
    second=0;//s
    tenMs=0;//10ms
	document.getElementById("show").innerHTML='';	
}

$("#timerClose").click(function(){
	clearInterval(timerForCallTest);
	$(".sidebarDiv").html("");
}) 
