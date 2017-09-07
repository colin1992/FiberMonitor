/**
 * 
 */
	   /**验证session是否有效,如果session过期，则强制回到登录界面**/
 $.ajax({
     url:'checkSession',
     type:'post', //数据发送方式
     dataType:'json', //接受数据格式
     async: false,
     success: function(json){
     	if(!json[0].status){
     		var txt="由于长时间未使用，当前登录信息已过期，请重新登录";
	    	    	 var option={
	    	   					title: "提示",
	    	   					btn: parseInt("0001",2),
	    	   					onOk: function(){//点击确认的执行方法
	    	   						localStorage.clear();
	    	   						window.location.href="Login.html";
	    	   					},
	    	  			       onClose:function(){
	    	  			    	    localStorage.clear();
	    	  					    window.location.href="Login.html";
	    	  					    
	    	  			       },
	    	   				}
	    	   	  window.wxc.xcConfirm(txt, "info", option);
     	}
     },
     error:function(XMLHttpRequest,Error)
	      {
	    	  
	     }
   });