
(function ($) {

   $.fn.pickList = function (options) {

      var opts = $.extend({}, $.fn.pickList.defaults, options);

      this.fill = function () {
         var option = '';

         $.each(opts.data, function (key, val) {
            option += '<option id=' + val.id + '>' + val.text + '</option>';
         });
         this.find('#pickData').append(option);
      };
      this.controll = function () {
         var pickThis = this;

      };
	  /*------将选择列表清空----*/
	  this.setEmpty=function(){
		  var p = this.find("#pickListResult option");
		  p.remove();
		  this.find('#pickData').empty();
	 };
	 /*--------重新给下拉列表赋值---------*/
	 this.reSet=function(data){
		 this.setEmpty();
		 var option = '';
          $.each(data, function (key, val) {
            option += '<option id=' + val.id + '>' + val.text + '</option>';
         });
         this.find('#pickData').append(option);
	 };
	 /*----------给已选择的列表赋值----------*/
	 this.setSelectedList=function(selectValues){
		 var option = '';
		 $.each(selectValues, function (key, val) {
	            option += '<option id=' + val.id + '>' + val.text + '</option>';
	         });
		 this.find('#pickListResult').append(option);
		
	 }
	 this.getLeftSelectValues = function () {
         var objResult = [];
         this.find("#pickData option:selected").each(function () {
            objResult.push({id: this.id, text: this.text});
         });
         return objResult;
      };
      this.getValues = function () {
         var objResult = [];
         this.find("#pickListResult option").each(function () {
            objResult.push({id: this.id, text: this.text});
         });
         return objResult;
      };
      this.init = function () {
         var pickListHtml =
                 "<div class='row'>" +
                 "  <div class='col-sm-5'>" +
                 "	 <select class='form-control pickListSelect' id='pickData' multiple></select>" +
                 " </div>" +
                 " <div class='col-sm-2 pickListButtons'>" +
                 "	<div><button id='pMatch' class='btn btn-primary btn-sm'>" + opts.match + "</button>" +
                 "	     <button id='pMatchRemove' class='btn btn-primary btn-sm'>" + opts.matchRemove + "</button></div>" +
                 " </div>" +
                 " <div class='col-sm-5'>" +
                 "    <select class='form-control pickListSelect' id='pickListResult' multiple></select>" +
                 " </div>" +
                 "</div>";

         this.append(pickListHtml);

         this.fill();
         this.controll();
      };

      this.init();
      return this;
   };

   $.fn.pickList.defaults = {
    	  match:'配对'    ,    
    	  matchRemove:'取消配对'
   };


}(jQuery));


