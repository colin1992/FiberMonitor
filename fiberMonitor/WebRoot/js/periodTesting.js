/*------------------生成左侧树----------------------*/
$(document).ready(function () {
	var hrefReload = "javascript:Reload()";
	leftTree = new dTree('leftTree');//创建一个对象.
	leftTree.config.folderLinks = true;
	leftTree.config.useCookies = false;
	leftTree.config.check = true;
	$.ajax({
		url: 'create_tree',
		type: 'post', //数据发送方式
		dataType: 'json', //接受数据格式
		async: false,
		success: function (json) {
			for (var JsonCount = 0; JsonCount < json.length; JsonCount++) {
				var equiId = "";//定义字符串来承接
				var NodeId = json[JsonCount].id;//读取节点id
				var nodeName = json[JsonCount].name;
				var parentId = json[JsonCount].pid;//读取节点pid
				if (parseInt(NodeId[0]) < 4) {  //显示到RTU
					if (NodeId == "0" || NodeId == 0) {
						leftTree.add(NodeId, parentId, "周期测试", hrefReload, "", "", "", "", true);
					}
					else if (parseInt(NodeId[0]) == 3) {//为RTU
						for (var index = 2; index < NodeId.length; index++) { //
							equiId += NodeId[index];//拼接字符串
						}
						hrefStaAddress = "javascript:getRouteByRtuId(" + parseInt(equiId) + ")";//
						leftTree.add(NodeId, parentId, nodeName, hrefStaAddress, "", "", "", "", false);
					}

					else {
						leftTree.add(NodeId, parentId, nodeName, "", "", "", "", "", false);
					}
				}

			}
			document.getElementById("leftTree").innerHTML = leftTree;
		},
		error: function (XMLHttpRequest, Error) {
			
		}
	});
});

//载入这个页面的时候，首先呈现的是GIS界面
$(".rightTable").css("display","none");
$(".mapDiv").css("display","block");

$("#backToGis").click(function () {
	$(".rightTable").css("display","none");
	$(".mapDiv").css("display","block");
})

/**点击光路管理节点，重载当前页面*/
function Reload() {
	$(".containerDiv").load("html/resource/periodTesting.html");
}
/**通过rtuId获取光路列表**/   
function getRouteByRtuId(rtuId){
	/***初始化周期测试相关的localStorage存储参数**/
	var optiPara = new Array();
	var timeTable = new Array();
	var periodCanIndex = new Array();
	localStorage.setItem('optiPara', JSON.stringify(optiPara));	//新建数组放到localStorage，用于存放各组的优化参数
	localStorage.setItem('timeTable', JSON.stringify(timeTable));	//新建数组放到localStorage，用于存放各组的时间表
	localStorage.setItem('periodCanIndex',JSON.stringify(periodCanIndex));//取消周期测试的光路id数组
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	$(".periodListCurves").css("display","none");
	$(".periodCurveShow").css("display","none");
	var trs = document.querySelectorAll("#periodTestingTableDetail tbody tr td");
	for (var i = 0; i < trs.length; i++) {
		trs[i].innerHTML = "";
	}//先清空
 	$.ajax({
            			type : "post",
            			async : false,  //异步请求 先执行后续操作，再执行sucess
            			url : "getRouteByRtuId",
            			dataType:"json",
            			data : {
            				      "id":rtuId, 
            				    },
            			success:function(json){
								if(json[0].status){
               						 var routePara=[]; 
               	 					 for(var i=1;i<json.length;i++){
               	 						 /***将进行周期测试和未进行的分开显示**/
               	 						 if(!json[i].isPeriod){
	               	 						 var equiPara=[
	               	 						                 json[i].id,
	               	 						                 json[i].name,
	           	    	 						             json[i].rtuName,
	           		   	 						             json[i].rtuOrder,
	           		 						                 json[i].status,
	           		 						                 json[i].length,
	           		 						                 json[i].isUplink,
	           		 						                 json[i].isBroken,
	           		 						                 json[i].rtuId,
	           		 						                 json[i].stationAId,
	           		 						                 json[i].stationAName,
	           		 						                 json[i].isPeriod,
	           		 						                 json[i].createTime,
	           		 						                 json[i].createUser,
	           		 						                 
	           		 						              ];
	               	 						routePara.push(equiPara)
	               	 						json.splice(i,1);//移除已经读取的
               	 					   }
               	 					}
               	 					 for(var i=1;i<json.length;i++){
              	 						 /***将进行周期测试和未进行的分开显示**/
              	 						 if(json[i].isPeriod){
	               	 						 var equiPara=[
	               	 						                 json[i].id,
	               	 						                 json[i].name,
	           	    	 						             json[i].rtuName,
	           		   	 						             json[i].rtuOrder,
	           		 						                 json[i].status,
	           		 						                 json[i].length,
	           		 						                 json[i].isUplink,
	           		 						                 json[i].isBroken,
	           		 						                 json[i].rtuId,
	           		 						                 json[i].stationAId,
	           		 						                 json[i].stationAName,
	           		 						                 json[i].isPeriod,
	           		 						                 json[i].createTime,
	           		 						                 json[i].createUser,
	           		 						                 
	           		 						              ];
	               	 							routePara.push(equiPara)
	               	 					}
              	 					}
               	 					 setTable(routePara);//分页写入表格
               						
               				   }
               			},
            			error:function(XMLHttpRequest,Error){
            			
            		   }
        });

  }
/************************实现分页的表格写入函数***************************/
function setTable(tableData){
	var nums = document.querySelectorAll(".periodTestingTableDetail tbody tr").length;
    var cells = document.getElementById("periodTestingTableDetail").rows.item(0).cells.length;
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
            var trs = document.querySelectorAll("#periodTestingTableDetail tbody tr");
            var tds = document.querySelectorAll("#periodTestingTableDetail tbody tr td");
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
/**------------------------选择光路/取消光路选择按键------------------------*/ 

function setPath(){
	var cells = document.getElementById("periodTestingTableDetail").rows.item(0).cells.length;//读取当前表格的列数
  var tab = document.getElementsByTagName("table")[0];
  var rows = tab.rows;
  if(cells==16){
  	
  	for(var i=0; i<rows.length;i++){
          rows[i].deleteCell(0);
     }
  	value=0;
    document.getElementById("selectPath").innerHTML = "选择光路";
  }
 
} 
/**获取当前系统时间**/
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	if (hour >= 0 && hour <= 9) {
		hour = "0" + hour;
	}
	if (minute >= 0 && minute <= 9) {
		minute = "0" + minute;
	}
	if (second >= 0 && second <= 9) {
		second = "0" + second;
	}
	var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		+ " " + hour + seperator2 + minute
		+ seperator2 + second;
	return currentdate;
}  
/**点击GIS局站  展开树**/
function openSpeficTree(stationId) {
	leftTree.config.folderLinks = false;
	leftTree.closeAll();
	$(".rightTable").css("display","block");
	$(".mapDiv").css("display","none");
	var stationid = "2_" + stationId;
	leftTree.openTo(stationid, false);
}    