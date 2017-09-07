/**
 * 
 */
/*********************生成树********************************/
            tree = new dTree('tree');//创建一个对象.
            tree.config.folderLinks=true;
            tree.config.useCookies=false;
            tree.config.check=true;
            $.ajax({
                url:'create_tree',
                type:'post', //数据发送方式
                dataType:'json', //接受数据格式
                async: false,
                success: function(json){
                    $(json).each(function(){
                    	for(var count=0;count<json.length;count++){
                    		var stationId=" ";//定义字符串来承接areaId
                    		 var nodeId=json[count].id;//读取节点id
                             var parentId=json[count].pid;//读取节点pid             
                             var hrefAddress="";//定义url指向js，当为区域时需要使用
                             if(parseInt(json[count].id[0])<3){//最多只到局站
                          	 if(parseInt(json[count].id[0])==2){//为局站
                          		 var stationName=json[count].name;
                          		for(var index=2;index<json[count].id.length;index++){ //读取areaId，    id新式为x_xxx,所以从第二位开始为areaId
                          			stationId+=json[count].id[index];//拼接字符串，获得字符串形式的areaId
                          		 }
                          		hrefAddress="javascript:selectWiringframeByStation('"+parseInt(stationId)+"','"+stationName+"')";
                          	   }
                          	if(parseInt(json[count].id[0])==0){//为根节点
                          		stationId="0";
                          		hrefAddress="javascript:reloadCurentpage()";
                          		json[count].name="配线架管理";
                          	   }
                            	var nodeName=json[count].name;

                        
                        tree.add(nodeId,parentId,nodeName,hrefAddress,"","","","",false);
                             }
                    	}
                    });
                }
            });
                document.getElementById("leftTree").innerHTML = tree;
                

/*********************写入表格的接口******************************/                
                function tableDataIn(tabledata){
                    var tableData = tabledata;                                     /*tableData为后台需渲染到表格中的所有数据*/
                    var nums = document.querySelectorAll(".wiringframeTableDetail tbody tr").length;
                    var cells = document.getElementById("wiringframeTableDetail").rows.item(0).cells.length;
                    var pages = Math.ceil(tableData.length/nums);
                    var thisData = function (curr) {
                        var tableDataCurr= [];
                        var last = curr*nums - 1;
                        last = last >= tableData.length ? (tableData.length - 1) : last;
                        tableDataCurr = tableData.slice(curr*nums - nums,last + 1);
                        return tableDataCurr;
                    };
                    
                    laypage({
                        cont: 'pageon',
                        pages: pages,
                        jump: function(obj) {
                            var currTableData = thisData(obj.curr);
                            var trs = document.querySelectorAll("#wiringframeTableDetail tbody tr");
                            var tds = document.querySelectorAll("#wiringframeTableDetail tbody tr td");
                            for (var i=0;i<tds.length;i++){
                                tds[i].innerHTML = "";
                            }
                            
                            for (var i = 0; i < currTableData.length; i++) {
                                var Ele = trs[i].children;
                                for(var j=0; j<cells-1; j++){
                                    Ele[0].innerHTML = i + 1;
                                    Ele[j+1].innerHTML = currTableData[i][j];
                                }
                            }
                        }
                    });
                }
 
  /************************叶节点执行的查询数据库并写入table的操作   *************************/  
                	var dataSearch=[];
                	var ifClickStation=false;
                	function selectWiringframeByStation(stationID,stationName){
                		     ifClickStation=true;
                             $(".rightTable").css("display","block");
                             $(".mapDiv").css("display","none");
                			 wiringframeStationId=stationID;
                			 var dataInput=[]; 
                			 var cableList = [];
                	  		 $.ajax({
                	          			type : "post",
                	          			async : false,  //异步请求 先执行后续操作，再执行sucess
                	          			url : "ResourceListDistributeFrame",                            /*修改为wiringframe对应url */
                	          			dataType:"json",
                	          			data : {stationId:stationID},
                	          			success:function(data){                                         
                	          				dataSearch=data;
                	          				for(i=0;i<data.length;i++){
                	             				dataInput.push([data[i].frame_id,  
                	             				                data[i].frame_name,
                	               				                data[i].stationId,
                	               				                data[i].cabinetId,	
                	               				                data[i].rackOrder,
                	               				    			data[i].port_number,	               				    			
                	               							    data[i].description,
                	               							    data[i].create_time,
                	               							    data[i].alter_time
                	               							    ]);  	             					             				             				
                	             				}
                	          			},
                	          			
                	              	});
                	  		tableDataIn(dataInput);	  	
                	}   

                 /**表格范围内禁止系统右键，使用自定义右键**/
                    $("#wiringframeTableDetail").hover(function(){
                    	 $(document).bind("contextmenu",function(e){   
                    		   return false;   
                    		 });
                    },
                    function(){
                    	$(document).unbind("contextmenu","")
                    });	
                    /****************************添加右键菜单功能***********************************/
                    $(".wiringframeTableDetail tbody tr").click(function(){
                    	 $(this).addClass("currtr").siblings().removeClass("currtr");
                    	 var selectId=this.children[1].innerHTML;
                    	 var stationId=this.children[3].innerHTML;
                    	 if(selectId!=""){
                    		 $(this).addClass("frame").siblings().removeClass("frame");
                    	 }
                    	 else{
                    		 $(this).removeClass("frame").siblings().removeClass("frame");
                    	 }
                    	var menu = new BootstrapMenu('.frame', {
                                actions: [{
                                    name: '修改配线架',
                                    onClick: function() {
                                    	 $(this).remove();
                                    	 $(".sidebarDiv").load("html/resource/wiringframeModify.html");
                                    }
                                },
                                {
                                    name: '删除配线架',
                                    onClick: function() {
                                    	 $(this).remove();
                                    	 var txt="警告：删除配线架将删除其下的所有资源！<br/>";
                            	    	 txt+="请谨慎选择，";
                            	    	 txt+="确认删除定点击确定键。"
                     				 var option={
                        	   					title: "提示",
                        	   					btn: parseInt("0011",2),
                        	   					onOk: function(){//点击确认的执行方法
                        	   					delFrame(selectId,stationId);  
                        	  			       }
                        	   		     }
                        	   		    window.wxc.xcConfirm(txt, "info", option);
                                    }
                                }]
                            });
                    }) 
                	 $(".wiringframeAdd").click(function () {	
                		  $(".sidebarDiv").load("html/resource/wiringframeAdd.html");
                	   });
                    /********************************弹出配线架修改sidebar ***********************************/
                    $(".wiringframeModify").click(function () {
                    		var trs = document.querySelectorAll("#wiringframeTableDetail tbody tr");
                    	    var frameId=""; 
                    	    for (var i=0;i<trs.length;i++){
                    	        if($(trs[i]).hasClass("currtr")){
                    	        	frameId=trs[i].children[1].innerHTML;
                    	        	i=trs.length;
                    	        }
                    	    }
                    	    if(frameId==""){
                       	    	var txt="请先选择您要修改的配线架";
                				var option={
                    	   					title: "提示",
                    	   					btn: parseInt("0001",2),
                    	   					onOk: function(){//点击确认的执行方法
                    	   						
                    	  			       }
                    	   				}
                    	   		  window.wxc.xcConfirm(txt, "info", option);
                       		   }
                    	    else{	
                    	       $(".sidebarDiv").load("html/resource/wiringframeModify.html");
                    	    }
                    	});
                    
                    $(".wiringframeDelete").click(function(){
                    	var trs = document.querySelectorAll("#wiringframeTableDetail tbody tr");
                    	 var selectId="";
                    	 var stationId="";
                    	    for (var i=0;i<trs.length;i++){
                    	     if($(trs[i]).hasClass("currtr")){
                    	    	selectId=trs[i].children[1].innerHTML;
                    	    	stationId=trs[i].children[3].innerHTML;
                    	    	i=trs.length;
                    	     }
                    	    }if(selectId=="")//未选择cable
                    		   {
                    	    	 var txt="请先选择您要删除的配线架";
             				 var option={
                 	   					title: "提示",
                 	   					btn: parseInt("0001",2),
                 	   					onOk: function(){//点击确认的执行方法
                 	   						
                 	  			       }
                 	   		 }
                 	   		window.wxc.xcConfirm(txt, "info", option);
                    		   }
                    	    else{
                    	    	 var txt="警告：删除配线架将删除其下的所有资源！<br/>";
                    	    	 txt+="请谨慎选择，";
                    	    	 txt+="确认删除定点击确定键。"
             				 var option={
                	   					title: "提示",
                	   					btn: parseInt("0011",2),
                	   					onOk: function(){//点击确认的执行方法
                	   					delFrame(selectId,stationId);  
                	  			       }
                	   		     }
                	   		    window.wxc.xcConfirm(txt, "info", option);
                        		 
                    	  }
                    });
                /**删除配线架**/    
                    function delFrame(frameId,stationId){
                    	 $.ajax({
	                  			type : "post",
	                  			async : false,  //异步请求 先执行后续操作，再执行sucess
	                  			url : "frame/delFrame",
	                  			dataType:"json",
	                  			data : {"frameId":frameId},
	                  			success: function(json){
	        		       	        var txt="";
	        				    	if(json[0].status){
	        				    		txt+="删除成功<br/>"
	        				    	}
	        				    	else{
	        				    		txt+="删除失败，失败原因："+json[0].err;
	        				    	}
	        					     var option = {
	        									title: "提示",
	        									btn: parseInt("0001",2),
	        									onOk: function(){//点击确认的执行方法
	        										reListTable(stationId);
	        									}
	        								}
	        						  window.wxc.xcConfirm(txt, "info", option);
	        				      },
	        				    error:function(XMLHttpRequest,Error)
	        				      {
	        				    	   
	        				    	    var txt="删除失败<br/>";
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
          function reListTable(stationId){
        	  selectWiringframeByStation(stationId,"");
          }           
          function reloadCurentpage(){
                   $(".containerDiv").load("html/resource/wiringframe.html");
          }

/**根据配线架名字查询配线架（点击局站搜索当前局站下面的配线架，点击根节点对数据库中所有的配线架进行模糊搜索）*/              	
                	  function searchFrame(){
  	                     var dataINPUT=[];
                		 var frameNAME=document.getElementById("frameNameSearch").value;
                		 if(frameNAME=="") {
                			  var txt="请输入查询条件";
         	            	  var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
         	            	window.wxc.xcConfirm(txt, "info", option);
                		 }
                		 else{
                			 if(dataSearch.length==0)   //点击根节点的情况
                				 {         
  		              				$.ajax({
  		              	              type : "post",
  		              	              async : false,  //异步请求 先执行后续操作，再执行sucess
  		              	              url : "ResourceSearchFrame",
  		              	              dataType:"json",
  		              	              data : {"frameNAME":frameNAME},
  		              	              success:function(data){
  		              	            	  
  		              	            	for(i=0;i<data.length;i++){
  		              	            		dataINPUT.push([data[i].frame_id,  
  		              	            		                data[i].frame_name,
  		              	            		                data[i].stationId,
  		              	            		                data[i].cabinetId,	
  		              	            		                data[i].rackOrder,
  		              	            		                data[i].port_number,	               				    			
  		              	            		                data[i].description,
  		              	            		                data[i].create_time,
  		              	            		                data[i].alter_time
  	                           							    ]);  
  	                         				}
  		              	            	if(dataINPUT==""){
  		              	            	  var txt="找不到符合条件的配线架";
  		              	            	  var option={
     				    	   					title: "提示",
     				    	   					btn: parseInt("0001",2),
     				    	   					onOk: function(){//点击确认的执行方法
     				    	   						
     				    	  			       }
     				    	   				}
     				    	   		      window.wxc.xcConfirm(txt, "info", option);
  		              	            	}
  		              	            	else tableDataIn(dataINPUT);
  		              	              },
  		              	          
  		              	          });

                				 }
                			 else{
                				 for(i=0;i<dataSearch.length;i++){
		  		              		 if(dataSearch[i].frame_name==frameNAME){
		  		              			dataINPUT.push([dataSearch[i].frame_id,  
		  		              			                dataSearch[i].frame_name,
		  		              			                dataSearch[i].stationId,
		  		              			                dataSearch[i].cabinetId,	
		  		              			                dataSearch[i].rackOrder,
		  		              			                dataSearch[i].port_number,	               				    			
		  		              			                dataSearch[i].description,
		  		              			                dataSearch[i].create_time,
		  		              			                dataSearch[i].alter_time]);              			              			
		                		          }
                		         } 
  		              		if(dataINPUT.length==0){
  		              		  var txt="当前局站找不到符合条件的配线架<br/>您可以尝试全局搜索";
          	            	  var option={
		    	   					title: "提示",
		    	   					btn: parseInt("0001",2),
		    	   					onOk: function(){//点击确认的执行方法
		    	   						
		    	  			       }
		    	   				}
          	            	window.wxc.xcConfirm(txt, "info", option);
  		              		 }  
  		            		 else {
  		            			tableDataIn(dataINPUT);  
  		            		  } 
                			 }
                		 }
                	 } 
                	  
  /*****************GIS功能****************/ 

 //载入这个页面的时候，首先呈现的是GIS界面
       $(".rightTable").css("display","none");
       $(".mapDiv").css("display","block");
       $("#backToGis").click(function(){
              $(".rightTable").css("display","none");
              $(".mapDiv").css("display","block");
       })

      function openSpeficTree(stationId){
          tree.config.folderLinks=false;
          tree.closeAll();
          $(".rightTable").css("display","block");
          $(".mapDiv").css("display","none");
          var stationid="2_"+stationId;
          tree.openTo(stationid,true);
      }
                	 