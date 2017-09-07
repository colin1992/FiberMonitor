/**
 * 
 */
/**=============全局变量=================**/
     var linkSelect=[
                      ["upLink","上行链路"],
                      ["downLink","下行链路"]
                    ];
     /**光路类型*/
     var typeSelect=[
                      ["true","false"],//上行
                      ["true","false"]//下行
                    ]
     /***光路选择菜单项**/
     var routeSelect=[
                       [//上行
                        [0,0],
                        [0,0]
                       ],
                       [//下行
                        [0,0],
                        [0,0]
                       ]
                     ];
     /**参数选择项**/
     var paraSelect=[
                      [//上行
                        [//在线
	                         ["thPara","门限参数"],//门限参数
	                         ["testPara","测试参数"]
                        ],
                        [//备纤
	                         ["thPara","门限参数"],//门限参数
	                         ["testPara","测试参数"]
                        ]
                      ],
                      [//下行
						[//在线
						 ["thPara","门限参数"],//门限参数
						 ["testPara","测试参数"]
						],
						[//备纤
						 ["thPara","门限参数"],//门限参数
						 ["testPara","测试参数"]
						]
                      ]
                    ];
     var upLink;
     var downLink;
	 /****定义已经配置的参数
	  * index 0为上行在线
	    index 1为上行备纤
	    index 2为下行在线
	    index 3为下行备纤
		**/

		var threshold=["","","",""];
		var testPara=["","","",""];
		/**上行链路的信息,为json对象，直接存储后台反馈的数据
		   包含以下信息：
		   			upLink.ids 长度为2的数组，表示上行链路在线和备纤的Id
		   			upLink.names 长度为2的数组，表示上行链路在线和备纤的name
		   			upLink.status 长度为2的数组，表示上行链路在线和备纤的状态信息
		**/
      /**=====载入页面时写入信息=======**/
		$(document).ready(function(){
			var url = document.location.href.split("//")[1];//通过读取浏览器地址栏来获取构建webSocket的地址
	 	    url =url.split(":")[0];
	 	    var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
	 	    if(!(ip.test(url)&&(RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256))){
		    		/**如果从浏览器获取到的不是IP地址，查询数据库**/
	 	    	$.ajax({
	 	    		type: 'post', //数据发送方式
	 	    		dataType: 'json', //接受数据格式
	 	    		async: false,
	 	    		url: 'getServerIp',
	 	    		data: '',
	 	    		success: function (json) {
	 	    			if (json[0].status) {
	 	    				url=json[0].ip;
	 	    			}
	 	    		}
	 	    	})
	         }
	 	    $("#P18").val(url);
	 	    $("#P19").val(url);
	 	/***获取保护组上下行链路四条光路的信息***/    
			$.ajax({
				type: "post",
				async: false,  //异步请求 先执行后续操作，再执行sucess
				url: "switch/getGroupRoute",
				dataType:"json",
				data: {
					"groupId":groupId,//groupId为当前选中的配对组，定义在switchConfigur.js中
				},
				success: function (json) {
					if (json[0].status) {
						upLink=json[0].upLink;
						downLink=json[0].downLink;
						$("#routeSelect").empty();
						$("#routeSelect").append(
									                 "<option value= "+upLink.ids[0]+" id='0'>"+upLink.names[0]+"</option>"+
									                 "<option value= "+upLink.ids[1]+" id='1'>"+upLink.names[1]+"</option>"
							                         );
						routeSelect[0][0][0]=upLink.ids[0];
						routeSelect[0][0][1]=upLink.names[0];
						routeSelect[0][1][0]=upLink.ids[1];
						routeSelect[0][1][1]=upLink.names[1];
						routeSelect[1][0][0]=downLink.ids[0];
						routeSelect[1][0][1]=downLink.names[0];
						routeSelect[1][1][0]=downLink.ids[1];
						routeSelect[1][1][1]=downLink.names[1];
						/**默认选中光路为上行的在线纤**/	
						var lIndex=parseInt($("#linkSelect").prop('selectedIndex'));//链路选择index
						var rIndex=parseInt($("#routeSelect").prop('selectedIndex'));
						$("#typeSelect").val(typeSelect[lIndex][rIndex]);
					}
				},
			})
			/**链路方向选择变化
			 **设置当前链路的方向
			**/
		   $("#linkSelect").change(function(){
			     resetRouteSelect();
			})
		  /**光路选择变化
		  ** 设置当前光路的状态以及当前能设置的参数项
		  **/
		   $("#routeSelect").change(function(){
			   resetParaSelect();
			   var lIndex=parseInt($("#linkSelect").prop('selectedIndex'));//链路选择index
			   var rIndex=parseInt($("#routeSelect").prop('selectedIndex'));
			   $("#typeSelect").val(typeSelect[lIndex][rIndex]);
		   })
			
		})
			var P11=[1,5,10,30,60,100,180];
		    var P12=[
				    		      [10,20,40,80,160],
					    		  [20,40,80,160,320],
					    		  [40,80,160,320,640],
					    		  [80,160,320,640,1280],
					    		  [160,320,640,1280,2560],
					    		  [640,1280,2560,5120,10240],
					    		  [1280,2560,5120,10240,20480]
		    		      ];
		    for (var count = 0; count < P11.length; count++) {
				if (count == 2) {
					$("#P11").append("<option value=" + P11[count] + " selected='selected'>" + P11[count] + "</option>");
				}
				else {
					$("#P11").append("<option value=" + P11[count] + ">" + P11[count] + "</option>");
				}
			}
			for (var index = 0; index < P12[2].length; index++) {
				if (index == 0) {
					$("#P12").append("<option value=" + P12[2][index] + " selected='selected'>" + P12[2][index] + "</option>");
				}
				else {
					$("#P12").append("<option value=" + P12[2][index] + ">" + P12[2][index] + "</option>");
				}
			}

			$("#P11").change(function () {
				var range = $(this).prop('selectedIndex');
				$("#P12").empty();
				for (var index = 0; index < P12[range].length; index++) {
					if (index == 0) {
						$("#P12").append("<option value=" + P12[range][index] + " selected='selected'>" + P12[range][index] + "</option>");
					}
					else {
						$("#P12").append("<option value=" + P12[range][index] + ">" + P12[range][index] + "</option>");
					}
				}
			})
/**cancel**/
$("#cancel").click(function(){
	$(".contentDiv").html("");
    $(".sidebarDiv").html("");	
})
$(".sidebar_close").click(function(){
     $(".contentDiv").html("");
     $(".sidebarDiv").html("");
 });
/**参数类型下拉框变化**/
$("#paraSelect").change(function(){
	var paraType=$(this).val();
	if(paraType=="thPara"){
		$("#thPara").css("display","block");
		$("#testPara").css("display","none");
	}
	else{
		$("#thPara").css("display","none");
		$("#testPara").css("display","block");
	}
}) 
/***重设参数下拉菜单***/
function resetParaSelect(){
	  var lIndex=parseInt($("#linkSelect").prop('selectedIndex'));//链路选择index
	  var rIndex=parseInt($("#routeSelect").prop('selectedIndex'));
	   $("#paraSelect").empty();
	   for(var i=0;i<paraSelect[lIndex][rIndex].length;i++){
			 $("#paraSelect").append(
	                 "<option value= "+paraSelect[lIndex][rIndex][i][0]+" id='0'>"+paraSelect[lIndex][rIndex][i][1]+"</option>");
	   }
	   var paraType= $("#paraSelect").val();
		if(paraType=="thPara"){
			$("#thPara").css("display","block");
			$("#testPara").css("display","none");
		}
		else{
			$("#thPara").css("display","none");
			$("#testPara").css("display","block");
		}
}
/***重设光路下拉菜单**/
function resetRouteSelect(){
	 var lIndex=parseInt($("#linkSelect").prop('selectedIndex'));
	 $("#routeSelect").empty();
	 for(var i=0;i<routeSelect[lIndex].length;i++){
		 $("#routeSelect").append(
                 "<option value= "+routeSelect[lIndex][i][0]+" id='0'>"+routeSelect[lIndex][i][1]+"</option>");
	 }
	 var rIndex=parseInt($("#routeSelect").prop('selectedIndex'));
	 $("#typeSelect").val(typeSelect[lIndex][rIndex]);
	 resetParaSelect();
}
/****/
function resetLinkSelect(){//重新设置下拉菜单
	 $("#linkSelect").empty();
	 for(var i=0;i<linkSelect.length;i++){
		 $("#linkSelect").append(
                "<option value= "+linkSelect[i][0]+" id='0'>"+linkSelect[i][1]+"</option>");
	 }
	 resetRouteSelect();
}
/***移除已设置的项**/
function removeSetedOption(){
	 var rIndex=$("#routeSelect").prop('selectedIndex');//当前光路的序号
	 var lIndex=$("#linkSelect").prop('selectedIndex');//链路序号
	 var pIndex=$("#paraSelect").prop('selectedIndex');//参数序号
	 if(!(routeSelect[0].length==1&&routeSelect.length==1&&paraSelect.length==1&&paraSelect[0][0].length==1)){//只剩最后一项时不移除
		 paraSelect[lIndex][rIndex].splice(pIndex,1);//移除当前光路的参数项
		 if(paraSelect[lIndex][rIndex].length==0){//某条光路的两种参数都已配置，移除当前光路
			 paraSelect[lIndex].splice(rIndex,1);
			 routeSelect[lIndex].splice(rIndex,1);
			 typeSelect[lIndex].splice(rIndex,1);
			 if(routeSelect[lIndex].length==0){//某个方向的两条光路参数均已配置完，移除当前方向
				 linkSelect.splice(lIndex,1);
				 routeSelect.splice(lIndex,1);//移除一个方向的光路
				 typeSelect.splice(lIndex,1);//移除一个方向的类型
				 paraSelect.splice(lIndex,1);//移除一个方向的参数
				 resetLinkSelect();//重新设置下拉菜单
			 }
			 resetRouteSelect();//重新设置下拉菜单
		 }
		 resetParaSelect();//重新设置下拉菜单
	 }
}
 /**门限确定键**/
 $("#thConfirm").click(function(){
	 var checkStatus=true;
	 var timeCheck=true;
	 for(var i=1;i<7;i++){
		 var pid="#th"+i;
		 var value=$(pid).val();
		 if(isNaN(value)||value==""){//有非数字
			 checkStatus=false;   
		     i=7;	
		 }
	 }
	 for(var i=1;i<7;i++){
		 var pid="#re"+i;
		 var value=$(pid).val();
		 if(isNaN(value)||value==""||value.indexOf(".") >= 0||parseInt(value)<0){//有非数字或负数
			 timeCheck=false;   
		     i=7;	
		 }
	 }
	 var txt="";
	 if(!(checkStatus&&timeCheck)){
		 txt="错误：存在非法输入。"
		 if(!checkStatus){
			 txt+="所有门限参数必须为数字，请核对后重新输入。";
		 }
		 else{
			 txt+="所有时间参数必须为正整数，请核对后重新输入。";
		 }
	 }
	 else{
		 /**读取当前选中的为上行链路还是下行链路**/
		 var link=$("#linkSelect").val();
		 /**上行链路两条光路的参数暂存序号为0,1 下行为2,3，从而存在偏移量**/
		 var routeIndex=(link=="upLink")?0:2;
		 /**序号0为在线  序号1为备纤**/
		 var rIndex=($("#typeSelect").val()=="true")?0:1;//当前光路的序号
		/**当前光路在参数暂存数组中的指针位置**/
		 var routePointer=parseInt(routeIndex)+rIndex;
		 /**当前光路的门限参数记录
		        顺序为：光路ID，光路类型，AT01-AT06，T3(二次告警间隔)，T4(重发告警间隔)
		 **/
		 var status=($("#typeSelect").val()=="true")?"1":"0";//在线或备纤  在线为1 备纤为0
		 threshold[routePointer]=[
		                    $("#routeSelect").val(),//当前光路ID
		                    status,//光路类型
		                  ];
		 for(var i=1;i<7;i++){//AT01-AT06
			 var pid="#th"+i;
			 threshold[routePointer].push($(pid).val());
		 }
		 var reConnectTime="";//T4 重发间隔(第一次发送失败)
		 var reSendTime="";//T3，二次告警(第一次发送成功，未处理)
		 for(var i=1;i<4;i++){
			 var pid="#re"+i;
			 var value=$(pid).val();
			 if(parseInt(value)<10){
				 value="0"+value;//格式化时间
				 reSendTime+=value;
			 }
		 }
		 /**T4重发告警时间**/
		 for(var i=4;i<7;i++){
			 var pid="#re"+i;
			 var value=$(pid).val();
			 if(parseInt(value)<10){
				 value="0"+value;//格式化时间
				 reConnectTime+=value;
			 }
		 }
		 threshold[routePointer].push(reSendTime);
		 threshold[routePointer].push(reConnectTime);
		 //保存后移除当前项
		 removeSetedOption();
		 txt="已保存您设置的门限参数，";
		 if(testPara[routePointer]==""){//设置的为门限参数，检查测试参数有没有设置
			 txt+="请继续为当前光路设置测试参数,设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发.";
		 }
		 else{//既设置了测试参数，又设置了门限参数，检查另外一条光路的参数是否设定
			 if(testPara[0]!=""&&testPara[1]!=""&&threshold[0]!=""&&threshold[1]!=""){//两条光路的参数都设置完成
				 if(testPara.indexOf("")<0&&threshold.indexOf("")<0){
					 txt+="您已完成该保护组的测试参数和门限参数设置，请点击\"提交\"键下发。";
				 }
				 else{
					 txt+="您已完成上行链路方向光路的测试参数和门限参数设置，请继续配置下行链路方向的参数，设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发."; 
				 }
				 
			 }
			 else if(testPara[2]!=""&&testPara[3]!=""&&threshold[2]!=""&&threshold[3]!=""){
				 if(testPara.indexOf("")<0&&threshold.indexOf("")<0){
					 txt+="您已完成该保护组的测试参数和门限参数设置，请点击\"提交\"键下发。";
				 }
				 else{
					 txt+="您已完成下行链路方向光路的测试参数和门限参数设置，请继续配置上行链路方向的参数，设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发."; 
				 }
			 }
			 else{
				 txt+="请设置下一条光路的参数，设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发."
			 }
		 }
	 }
	 var option = {
				title: "提示",
				btn: parseInt("0001",2),
				onOk: function(){//点击确认的执行方法
				}
			}
     window.wxc.xcConfirm(txt, "info", option);
 })
 /**验证IP**/
function validateForm() {
    return $("#testParaForm").validate({
        rules: {
            P18: {
            	required: true,
                ipv4: true
            },
            P19: {
                required: true,
                ipv4: true
            }
        },
        messages: {
        	P18: {
                required: "",
                ipv4: ""
            },
            P19: {
                required: "",
                ipv4: ""
            }
        }
    }).form();
}
 /**测试参数确定键**/
  $("#testConfirm").click(function(){
	 var checkStatus=true;
	 var ipCheck=true;
	 /**检查参数合法性**/
	 for(var i=14;i<18;i++){
		 var pid="#P"+i;
		 var value=$(pid).val();
		 if(isNaN(value)||value==""){//有非数字
			 checkStatus=false;   
		     i=18;	
		 }
	 }
	 /**验证IP合法性**/
	 ipCheck=validateForm();
	 var txt="";
	 if(!(checkStatus&&ipCheck)){
		 txt="错误：存在非法输入。"
		 if(!checkStatus){
			 txt+="所有门限参数必须为数字，请核对后重新输入。";
		 }
		 else{
			 txt+="IP地址非法，请核对后重新正确的IP地址。";
		 }
	 }
	 /**参数合法，读取参数**/
	 else{
		 /**读取当前选中的为上行链路还是下行链路**/
		 var link=$("#linkSelect").val();
		 /**上行链路两条光路的参数暂存序号为0,1 下行为2,3，从而存在偏移量**/
		 var routeIndex=(link=="upLink")?0:2;
		 /**序号0为在线  序号1为备纤**/
		 var rIndex=($("#typeSelect").val()=="true")?0:1;//当前光路的序号
		 /**当前光路在参数暂存数组中的指针位置**/
		 var routePointer=parseInt(routeIndex)+rIndex;
		 /**当前光路的测试参数记录 
		        顺序为：PS(默认取1)  P21-P27 IP1 IP2
		 **/
		 testPara[routePointer]=[1];
		 for(var i=11;i<20;i++){
			 var pid="#P"+i;
			 testPara[routePointer].push($(pid).val());
		 }
		 //保存后移除当前项
		 removeSetedOption();
		 txt="已保存您设置的测试参数，";
		 if(threshold[routePointer]==""){//设置的为测试参数，检查门限参数有没有设置
			 txt+="请继续为当前光路设置门限参数,设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发.";
		 }
		 else{//既设置了测试参数，又设置了门限参数，检查另外一条光路的参数是否设定
			 if(testPara.indexOf("")<0&&threshold.indexOf("")<0){//两条光路的参数都设置完成
				 txt+="您已完成该保护组的测试参数和门限参数设置，请点击\"提交\"键下发。";
			 }
			 else{
				 txt+="请设置下一条光路的参数，设置完成后点击\"确认\"键保存。配置完保护组所有光路的门限参数和测试参数后点击\"提交\"键下发."
			 }
		 }
	 }
	 var option = {
				title: "提示",
				btn: parseInt("0001",2),
				onOk: function(){//点击确认的执行方法
				}
			}
     window.wxc.xcConfirm(txt, "info", option);
 })
 /***查找某个元素在数组中出现位置的索引
  * @param: array 目标数组
  *        checkItem 目标元素
  *@return indexs 数组，目标元素各位置索引，如果不存在返回[]        
  * 
  * **/
  function getItemIndexs(array,checkItem){
		  var indexs=[];
		  if(array.indexOf(checkItem)>-1){
			  var index=array.indexOf(checkItem);
			  indexs.push(index);
			  while(index>-1){
				  index=array.indexOf(checkItem,index+1);
				  if(index>-1){
					indexs.push(index);
				  }
				}
		  }
		   return indexs;
	  }
 /*******提交*************
   验证两条光路的两项参数是否设置完成
   设置完成后下发，或者提示存在未设置的参数项目
 */
 $("#submit").click(function(){
	//两条光路的参数都设置完成 下发参数
	 if(testPara.indexOf("")<0&&threshold.indexOf("")<0){
		 $.ajax({
				url: "switch/setGroupPara",
				type: 'post', //数据发送方式
				dataType: 'json', //接受数据格式
				async: false,
				data: {
					"threshold": JSON.stringify(threshold),
					"testPara": JSON.stringify(testPara),
				},
				success: function (json) {
					if (json[0].status) {
						$(".contentDiv").html("");
					    $(".sidebarDiv").html("");
					    threshold=["","","",""];
					    testPara=["","","",""];
						var txt = "您已成功下发当前保护组的障碍告警测试参数 ，已为当前保护组启动障碍告警测试<br/>";
						var option = {
							title: "提示",
							btn: parseInt("0001", 2),
							onOk: function () {//点击确认的执行方法
							}
						}
						window.wxc.xcConfirm(txt, "info", option);
					}
					else {
						var txt = "配置失败,失败原因：" + json[0].err;
						var option = {
							title: "提示",
							btn: parseInt("0001", 2),
							onOk: function () {//点击确认的执行方法
							}
						}
						window.wxc.xcConfirm(txt, "info", option);
					}
				},
				error: function (XMLHttpRequest, textStatus) {
					var txt = "配置失败,失败原因：";
					if (XMLHttpRequest.status == 401) {
						txt += "您不具有当前操作的权限<br/>";
					}
					else {
						txt += "网络错误<br/>";
						txt += "状态码：" + XMLHttpRequest.status;
					}
					var option = {
						title: "提示",
						btn: parseInt("0001", 2),
						onOk: function () {//点击确认的执行方法

						}
					}
					window.wxc.xcConfirm(txt, "info", option);
				}
			});
			
		}
	 
	 /**存在尚未配置完成的参数，提示待配置的参数项**/
	 else{
		 var txt="存在未配置完成的参数,请先配置以下参数：";
		 /**读取所有未设置测试参数的索引**/
		 var testIndexs=getItemIndexs(testPara,"");
		 /**未设置门限参数的索引**/
		 var thresIndexs=getItemIndexs(threshold,"");
		 /**所有参数均未配置**/
		 if(testIndexs.length==4&&thresIndexs.length==4){
			 txt+="上行和下行四条光路的测试参数;";
		 }
		 /**测试参数均未配置，查找门限参数未配置的光路**/
		 else if(testIndexs.length==4){
			 txt+="上行和下行四条光路的测试参数;";
			 /**上行链路**/
			 if(thresIndexs.indexOf(0)>-1){
				if(thresIndexs.indexOf(1)>-1){
					txt+="两条上行链路的门限参数;"
				}
				else{
					txt+="上行链路"+upLink.names[0]+"的门限参数;"
				}
					 
			}
			else if(thresIndexs.indexOf(1)>-1){
				txt+="上行链路"+upLink.names[1]+"的门限参数;"
			}
			if(thresIndexs.indexOf(2)>-1){
				if(thresIndexs.indexOf(3)>-1){
					txt+="两条下行链路的门限参数;"
				}
				else{
					txt+="下行链路"+downLink.names[0]+"的门限参数;"
				}
						 
			}
			else if(thresIndexs.indexOf(3)>-1){
				txt+="下行链路"+downLink.names[1]+"的门限参数;"
			}
		 
		}
		 /**门限参数均未配置完成，查找未配置的测试参数**/
		 else if(thresIndexs.length==4){
			 txt+="上行和下行四条光路的门限参数;";
			 /**上行链路**/
			 if(testIndexs.indexOf(0)>-1){
				if(testIndexs.indexOf(1)>-1){
					txt+="两条上行链路的测试参数;"
				}
				else{
					txt+="上行链路"+upLink.names[0]+"的测试参数;"
				}
					 
			 }
			 else if(testIndexs.indexOf(1)>-1){
				txt+="上行链路"+upLink.names[1]+"的测试参数;"
			 }
			 if(testIndexs.indexOf(2)>-1){
				if(testIndexs.indexOf(3)>-1){
					txt+="两条下行链路的测试参数;"
				}
				else{
					txt+="下行链路"+downLink.names[0]+"的测试参数;"
				}
						 
			 }
			 else if(thresIndexs.indexOf(3)>-1){
				txt+="下行链路"+downLink.names[1]+"的测试参数;"
			 }
		 }
		 /**都有部分配置完成，查找到底细节**/
		 else{
			 /**上行链路**/
			 if(testIndexs.indexOf(0)>-1){
				if(testIndexs.indexOf(1)>-1){
					txt+="两条上行链路的测试参数;"
				}
				else{
					txt+="上行链路"+upLink.names[0]+"的测试参数;"
				}
					 
			 }
			 else if(testIndexs.indexOf(1)>-1){
				txt+="上行链路"+upLink.names[1]+"的测试参数;"
			 }
			 if(testIndexs.indexOf(2)>-1){
				if(testIndexs.indexOf(3)>-1){
					txt+="两条下行链路的测试参数;"
				}
				else{
					txt+="下行链路"+downLink.names[0]+"的测试参数;"
				}
						 
			 }
			 else if(thresIndexs.indexOf(3)>-1){
				txt+="下行链路"+downLink.names[1]+"的测试参数;"
			 }
			 /**上行链路**/
			 if(thresIndexs.indexOf(0)>-1){
				if(thresIndexs.indexOf(1)>-1){
					txt+="两条上行链路的门限参数;"
				}
				else{
					txt+="上行链路"+upLink.names[0]+"的门限参数;"
				}
					 
			}
			else if(thresIndexs.indexOf(1)>-1){
				txt+="上行链路"+upLink.names[1]+"的门限参数;"
			}
			if(thresIndexs.indexOf(2)>-1){
				if(thresIndexs.indexOf(3)>-1){
					txt+="两条下行链路的门限参数;"
				}
				else{
					txt+="下行链路"+downLink.names[0]+"的门限参数;"
				}
						 
			}
			else if(thresIndexs.indexOf(3)>-1){
				txt+="下行链路"+downLink.names[1]+"的门限参数;"
			}
		 }
		txt[txt.length-1]="。";//加句号
		 var option = {
					title: "提示",
					btn: parseInt("0001",2),
					onOk: function(){//点击确认的执行方法
					}
				}
	     window.wxc.xcConfirm(txt, "info", option);
	 }
	
 })