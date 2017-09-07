/**
 * 给时间表中的开始时间项赋值为当期系统时间
 */$(document).ready(function(){
	var currentDate = new Date();
	var currentTime="";  
	var month=currentDate.getMonth()+1;//返回的为0-11
	var date=currentDate.getDate();//返回的为0-31
	var hour=currentDate.getHours();
	var minute=currentDate.getMinutes()+1;//当前时间的基础上多加一分钟
	if(minute==60){
		minute=0;
		hour+=1;
		}
	var second=currentDate.getSeconds();
	var dateString="0";
	var monthString="0";
	var hourString="0";
	var minuteString="0";
	var secondString="0";
	if(month<10){
		monthString+=month.toString();
	}else{
			monthString=month.toString();
	}
	if(date<10){
		dateString+=date.toString();
	}else{
	    dateString=date.toString();
	}
	if(hour<10){
		hourString+=hour.toString();
	}else{
	    hourString=hour.toString();
	}
	if(minute<10){
		minuteString+=minute.toString();
	}else{
	    minuteString=minute.toString();
	}
	if(second<10){
		secondString+=second.toString();
	}else{
		secondString=second.toString();
	}
	
	currentTime+=currentDate.getFullYear().toString()+monthString+dateString+hourString+minuteString+secondString;
	$("#startTime").val(currentTime);
  });