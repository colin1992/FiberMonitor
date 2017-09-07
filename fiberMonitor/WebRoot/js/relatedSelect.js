/**
 * jQuery 插件 级联下拉列表
 创建时间2016/7/8
 * 
 说明：三级级联下拉选择，
 使用方式为，xxLevel后为各级联动的下拉菜单的名称
组
 $(function(){
   $().related({
		firstLevel:"P11",  //select的名
		secondLevel:"P12",
		thirdLevel:"",
	  });
   
});
 同时还需定义三级下拉框的数
var firstLevelArr= testRange;//给一级菜单数组赋值
var secondLevelArr= pluseWidth;//给二级菜单数组赋值
var thirdLevelArr= test3;//给二级菜单数组赋值
 * 
 **/
(function($){
$.fn.related=function(options){
	var options=$.extend({},$.fn.related.defaults,options);	
	//判断是否给了父级select下拉列表的ID
	
	var s1=$("#"+options.firstLevel);//读取一级select
	var s2="";//定义二级菜单
     var s3="";//定义三级菜单
	//判断是否存在2级联动菜单
	if(options.secondLevel!="")  //此处select的名为select的名
		s2=$("#"+options.secondLevel);	
	//清空一级下拉列表
	if(options.thirdLevel!="")
	s3=$("#"+options.thirdLevel);	
	s1[0].options.length=0;	
	
	//遍历给一级下拉列表赋值
	$.each(firstLevelArr,function(i,data){						 
		if(i==4){
			       /*给一级下拉设置默认选中值为60*/
				$("<option value='"+data+"'"+"selected ='selected'>"+data+"</option>").appendTo("#"+s1[0].id);
			}else{
					$("<option value='"+data+"'>"+data+"</option>").appendTo("#"+s1[0].id);
		    }
	});
	var index1 = "" ;
	//给一级下拉列表添加 事件
	s1.change(function(){
		index1 = this.selectedIndex;//读取当前一级菜单的索引
		//二级列表赋值
		if(s2!=""){
			s2[0].options.length=0;//改变值前清空原有的值
			/*选择对应一级菜单索引的数组，给二级菜单遍历赋值*/
	        $.each(secondLevelArr[index1] , function(i,data){
					$("<option value='"+data+"'>"+data+"</option>").appendTo("#"+s2[0].id);	
				});
	     }
		 
	//存在三级下拉选择
		if(s3!=""){
			s3[0].options.length=0;
			/*一级菜单改变时将其切换一大组*/
		      $.each( thirdLevelArr[index1][0] , function(i,data){
				 	$("<option value='"+data+"'>"+data+"</option>").appendTo("#"+s3[0].id);	
				});
	    }
	}).change();
	//如果存在3级列表就给二级列表添加change事件
	if(s3!=""){
		var index2="";
		s2.change(function(){
			s3[0].options.length=0;//每次s2改变的时候将s3清空
			index2=this.selectedIndex;//读取s2当前的索引号
			
				$.each(thirdLevelArr[index1][index2] , function(i,data){
					
					$("<option value='"+data+"'>"+data+"</option>").appendTo("#"+s3[0].id);	
				});
			
		}).change; 
		 
}
	
};

})(jQuery);