 /**
  * 全局变量,当前选中的输入框DIV**/ 
var thisBox=$(".routebox .routebox-content .routebox-input.route-start");

//百度地图API功能
var marArea=document.getElementById("mapArea");
var map = new BMap.Map(marArea, {minZoom:6,maxZoom:14});    // 创建Map实例，设定缩放大小限制
map.centerAndZoom(new BMap.Point(104.06, 30.67), 9);           // 初始化地图，设置中心点坐标和地图缩放级别
map.enableScrollWheelZoom(true);                               //开启鼠标滚轮缩放
map.addControl(new BMap.NavigationControl());         //添加缩放按钮
//左上角比例尺控件
var scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
map.addControl(scaleControl); 
//测距工具
var disTool=new BMapLib.DistanceTool(map);
//获取坐标

var move=true;
var firstEnter=true;
function getMapPoint(){
	if($("#point").text()=="获取坐标"){
		$("#point").text("关闭坐标");
		$('#tip').css('display','block');
		var top=0;
		var left=0;
		move=true;
		if(firstEnter){
			$("#mapArea").mousemove(function(e){
				if($("#point").text()=="关闭坐标"&&move){
					    top = e.pageY+5;
					    left = e.pageX+5;
					    $('#tip').css({
						'top' : top + 'px',
					    'left': left+ 'px'
					   });
				}else{
					return true;
				}
			 });
			$("#mapArea").click(function(e){
				move=(!move);
			});
			 map.addEventListener("mousemove",function(e){
				 if(move){
					 $('#tip').text(e.point.lng + "," + e.point.lat); 
				 }
			});
		}
		
	}else{
		$("#point").text("获取坐标");
		 $('#tip').css('display','none');
	}
	firstEnter=false;	
}

//右下角缩略图控件
var overView = new BMap.OverviewMapControl();
var overViewOpen = new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
map.addControl(overView);         
map.addControl(overViewOpen);     

//复杂的自定义覆盖物
function ComplexCustomOverlay(point, text, mouseoverText){
  this._point = point;
  this._text = text;
  this._overText = mouseoverText;
  this._id=0;
}
/***
 *实例继承自Overlay类 
 */
ComplexCustomOverlay.prototype = new BMap.Overlay();
/**
 * 增加设置ID的方法
 */
ComplexCustomOverlay.prototype.setId = function(id) {
	this._id=id;
}
/**
 * 增加获取ID的方法
 */
ComplexCustomOverlay.prototype.getId = function() {
	return this._id;
}
/**
 * 初始化该类
 */
ComplexCustomOverlay.prototype.initialize = function(map){
	  this._map = map;
	  var div = this._div = document.createElement("div");
	  div.style.position ="absolute";
	  div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);
	  div.style.backgroundColor = "#EE5D5B";
	  div.style.border = "1px solid #BC3B3A";
	  div.style.color = "white";
	  div.style.height = "18px";
	  div.style.padding = "2px";
	  div.style.lineHeight="18px";
	  div.style.whiteSpace ="nowrap";
	  div.style.MozUserSelect="none";
	  div.style.fontSize = "13px"
	  var span = this._span = document.createElement("span");
	  div.appendChild(span);
	  span.appendChild(document.createTextNode(this._text));      
	  var that = this;//此处的that为该覆盖物对象
	  var arrow = this._arrow = document.createElement("div");
	  arrow.style.background = "url(../../images/label.png) no-repeat";
	  arrow.style.position = "absolute";
	  arrow.style.width = "11px";
	  arrow.style.height = "10px";
	  arrow.style.top = "22px";
	  arrow.style.left = "10px";
	  arrow.style.overflow = "hidden";
	  div.appendChild(arrow);
	  /**
	       鼠标移动到label
	  **/
	  div.onmouseover = function(){
		    this.style.backgroundColor = "#6BADCA";
		    this.style.borderColor = "#0000ff";
		    this.getElementsByTagName("span")[0].innerHTML = that._overText;
		    arrow.style.backgroundPosition = "0px -20px";
	  }
	  /**
	  鼠标移除label
	  **/
	 div.onmouseout = function(){
		    this.style.backgroundColor = "#EE5D5B";
		    this.style.borderColor = "#BC3B3A";
		    this.getElementsByTagName("span")[0].innerHTML = that._text;
		    arrow.style.backgroundPosition = "0px 0px";
	  }
	  /**
	   点击label
	  **/
	 div.onclick = function(){
		   // alert(that.getId())
	  }
	  map.getPanes().labelPane.appendChild(div);
	  return div;
}
ComplexCustomOverlay.prototype.draw = function(){
  var mp = this._map;
  var pixel = mp.pointToOverlayPixel(this._point);
  this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
  this._div.style.top  = pixel.y - 30 + "px";
}
/**
 *自定义marker类
 主要是为了添加id属性  
 **/
 function diyMarker(point){
	//构造函数调用Marker类的构造函数
	 BMap.Marker.call(this,point);
	 this._id=0;
 }
 /**
   该类继承自Marker
 **/
 diyMarker.prototype = new BMap.Marker();
 /**
 增加设置Id的方法
 */
 diyMarker.prototype.setId=function(id) {
	 this._id=id;
 }
 /**
 增加获取Id的方法
 */
 diyMarker.prototype.getId=function(){
	 return this._id;
 }
/**
 * 
 获取局站坐标信息
 ***/
$.ajax({
	url:'../../getMapPoints',
	type:'post', //数据发送方式
	dataType:'json', //接受数据格式
	async: false,
	success: function(json){
		if(json[0].status){
		var cablePoint=json[0].points;
		plotMarker(cablePoint);
		getCableLines();
		}
	},
	  error:function(XMLHttpRequest,textStatus){
		  
	  }
});
/**
 *获取光缆坐标信息 
 ***/
function getCableLines(){
	$.ajax({
		url:'../../getCableLines',
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		async: false,
		success: function(json){
			if(json[0].status){
			var cableLines=json[0].cables;
			plotLine(cableLines);
			}
		},
		error:function(XMLHttpRequest,textStatus){
			
		}
	});
}

/**
 *绘制局站 
 
 ***/
function plotMarker(cablePoint){
	if(cablePoint.length>0){
		map.centerAndZoom(new BMap.Point(cablePoint[0].lng, cablePoint[0].lat), 9);           // 如果存在局站，设置中心点坐标为第一个局站的位置
	}
	for(var i = 0; i < cablePoint.length; i++){
		var lng = cablePoint[i].lng;
		var lat = cablePoint[i].lat;
		var pointName = cablePoint[i].name;
		var pointId=cablePoint[i].id;
		var position = new BMap.Point(lng, lat);
		var marker = new diyMarker(position);//新建自定义marker; 
		marker.setId(pointId);//设置makerId    
		map.addOverlay(marker);//绘制标记点
		var pointLabel =new BMap.Label(pointName, {offset:new BMap.Size(20,-10)});
		pointLabel.setStyle({background:"#3385ff",color :"#ffffff",fontSize : "14px",borderColor:"blue"})  
		marker.setLabel(pointLabel);//绘制标签
		/**
		 * 点击局站
		 */
		marker.addEventListener("click", function(){    
			 var box=$(thisBox).find("input[type='text']");
			 $(box).val(this.getLabel().getContent()+"(id:"+this.getId()+")"); 
			 $(thisBox).find("div[class='input-clear']").show();
		});
		
	}
}


/** 
 绘制光缆
 起始点相同的光缆用平行方式错开
 @param cableLine 光缆的起始点坐标
        cableLine 为JSON数组
 **/
function plotLine(cableLine) {
	for (var i = 0; i < cableLine.length; i++) {
		var alng = cableLine[i].pointA[0];
		var alat = cableLine[i].pointA[1];
		var blng = cableLine[i].pointB[0];
		var blat = cableLine[i].pointB[1];
		var aPosition = new BMap.Point(alng, alat);
		var bPosition = new BMap.Point(blng, blat);
		var points = [aPosition, bPosition];
		var lineCount = cableLine[i].count;
		var lineNames = cableLine[i].name;
		var lineIds = cableLine[i].id;
		var marks=cableLine[i].landmarks;//光缆地标
		for (var j = 0; j < lineCount; j++) {
			var tempPoints = [aPosition];//全部的点
			for(var markIndex=0;markIndex<marks[j].length;markIndex++){//取每条光缆的
				var position=new BMap.Point(marks[j][markIndex].lng,marks[j][markIndex].lat);
				tempPoints.push(position);//加入地标点
				var pointName=marks[j][markIndex].name;
				var myIcon = new BMap.Icon("../../images/landmark.png", new BMap.Size(23,25), {    
					  offset: new BMap.Size(10, 15),    
					  imageOffset: new BMap.Size(0, -10)   // 设置图片偏移    
					 });      
					// 创建标注对象并添加到地图   
				var marker = new BMap.Marker(position, {icon: myIcon}); 
				map.addOverlay(marker);//绘制标地标点
				var pointLabel = new BMap.Label(pointName, { offset: new BMap.Size(20, -10) });
				marker.setLabel(pointLabel);//绘制标签
			}
			
			var cutPoint =[];//标记光缆名字的位置
			if(marks[j].length==0){//如果没有地标，则添加中点作为偏移量
				var centerlng = (aPosition.lng + bPosition.lng) / 2 + Math.abs(aPosition.lng - bPosition.lng) * Math.pow(-1, j) * 0.05 * j;
				var centerlat = (aPosition.lat + bPosition.lat) / 2;
				tempPoints.push(new BMap.Point(centerlng,centerlat));//如果没有地标，则添加偏移量
				forCut = [[aPosition.lng, aPosition.lat], [centerlng, centerlat]];
				cutPoint = lineDiv(forCut, j);
			}
			else{
				var cIndex=parseInt(tempPoints.length/2);//光缆途经点的中点index
				forCut=[[tempPoints[cIndex-1].lng, tempPoints[cIndex-1].lat], [tempPoints[cIndex].lng, tempPoints[cIndex].lat]];
				cutPoint=lineDiv(forCut,1);
			}
			tempPoints.push(bPosition);//加入终点
			var newPolyline = new BMap.Polyline(tempPoints, { strokeColor: "blue", strokeWeight: 2, strokeOpacity: 0.8 });
			map.addOverlay(newPolyline);
			var labelPosition = new BMap.Point(cutPoint[0], cutPoint[1]);
			var myCompOverlay = new ComplexCustomOverlay(labelPosition, lineNames[j], lineNames[j]);
			myCompOverlay.setId(lineIds[j]);
			map.addOverlay(myCompOverlay);
		}
	}
};
/**
 * 递归找等分点**/
function lineDiv(point,cutCount){
	if(cutCount==0)
	   return point[1];
	else 
	{
	   var cutLng=(point[0][0]+point[1][0])/2;
	   var cutLat=(point[0][1]+point[1][1])/2;
	   var cutPoint=[cutLng,cutLat];
	   return lineDiv([point[0],cutPoint],cutCount-1)
	}
		
}
/**
 * 设置站点输入框相关**/
$(document).ready(function(){
	 /***
	    * 增加站点*/
	 $(".route-input-add-icon").click(function(){
		    if($(".routebox-inputs").children().length<=10){
		    	var routeThrough=document.createElement("div");
			    routeThrough.className="routebox-input route-through";
			    $(routeThrough).append(
			    		 "<div class='route-input-icon'> </div> "+
			    		 "<input autocomplete='off' placeholder='请选择途经点' class='route-through-input' type='text'>"+
			    		 "<div class='input-clear' title='清空'> </div>"+
			    		 "<div class='route-input-remove-icon' data-index='1' title='移除站点'></div>"
			    );
			    $(".routebox .routebox-content .routebox-input.route-end").before(routeThrough);
		    }
    });
	 /**
		* 移除站点
		* 应用live绑定事件可以使得新增加的元素同样具有该属性**/
	$(".route-input-remove-icon").live('click', function(){
		$(this).parent().remove();//移除当前节点的父节点，也就是移除了一个输入项
	});
	/**
	* 起点终点反转
	* **/
	$(".routebox-revert-icon").click(function(){
		   var startStation=$(".route-start-input").val();
		  //alert($(".route-start-input").val()+"\nend:"+$(".route-end-input").val())
		   $(".route-start-input").val($(".route-end-input").val());
		   $(".route-end-input").val(startStation);
		})
  /**
   * 点击起点输入框**/	
	$(".routebox .routebox-content .routebox-input.route-start").click(function() {
		thisBox=this;
    });
  /**
	 * 点击途经站点输入框**/
	$(".routebox .routebox-content .routebox-input.route-through").live('click', function(){
		thisBox=this;
	});
 /**
   * 点击终点输入框**/
	$(".routebox .routebox-content .routebox-input.route-end").click(function(){
		thisBox=this;
	});
 /**
  清空站点内容
  **/
	$(".searchbox-content .input-clear").live('click', function(){
		var box=$(this).parent().find("input[type='text']");//通过获取当前clear图标的父节点来寻找输入框
		$(box).val("");
		$(this).hide();
	})
	/**
	 * 点击提交按键**/
	$("#set-button").click(function(){
		    var IDs=[];
		    //获取所选局站的ID
		    $(".routebox-inputs").children().each(
		    	function(){
		    	  var inValue=$(this).find("input[type='text']").val().split("id:")[1];
		    	  IDs.push(parseInt(inValue.split(")")[0]));
		    	}
		    );
		    $.ajax({
				    url:'../../setRoute',
			        type:'post', //数据发送方式
			        dataType:'json', //接受数据格式
			        async: false,
			        data:{
			        	    "id":JSON.stringify(IDs),
			             },
			         success: function(json){
			        	var crossStation=[];
			        	var route=[];
			        	var topological=[];
			        	var preparatoryId=[];
			        	var framePortOrder=[];
			        	if(json[0].status){
			        		for(var count=1;count<json.length;count++){
			        			route.push(json[count].rtuName+"_"+json[count].rtuPortOrder+"——"+json[count].frameName+"_"+json[count].framePortOrder);
			        			var top=[json[count].topologicalRoute,json[count].topologicalPoint];
			        			topological.push(top);//拓扑图+
			        			preparatoryId.push(json[count].preparatoryId);//preparatoryId
			        			framePortOrder.push(json[count].framePortOrder);//framePortOrder
			        		}
			        		crossStation.push(json[1].topologicalStation);//局站id
			        		localStorage.setItem('route', JSON.stringify(route));
			        		localStorage.setItem('topological', JSON.stringify(topological));
			        		localStorage.setItem('preparatoryId', JSON.stringify(preparatoryId));
			        		localStorage.setItem('framePortOrder', JSON.stringify(framePortOrder));
			        		localStorage.setItem('crossStation', JSON.stringify(crossStation));
			        		var routeSetedPara=[];
			        		localStorage.setItem("routeSetedPara",JSON.stringify(routeSetedPara));
			        		$(".preRouteList").load("selectRoute.html");
			   	           
			        	 }
			        	else{
			        		      var txt= "您选择的站点不存在可用光路，请核对后重试";
			        			  var option = {
			        						title: "提示",
			        						btn: parseInt("0001",2),
			        						onOk: function(){//点击确认的执行方法
			        						}
			        					}
			        			  window.wxc.xcConfirm(txt, "info", option);
			        		}
			         },
			         error:function(XMLHttpRequest,textStatus){
			   		  var txt= "设置错误，网络故障\n";
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
	 })
})
