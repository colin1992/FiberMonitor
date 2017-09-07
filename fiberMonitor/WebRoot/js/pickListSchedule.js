
(function ($) {

   $.fn.pickList = function (options) {

      var opts = $.extend({}, $.fn.pickList.defaults, options);

      this.fill = function () {
         var option = '';

         $.each(opts.data, function (key, val) {
            option += '<option id=' + val.id + '>' + val.text + '</option>';
         });
         this.find('#pickData').append(option);
         
         var optionRight = '';
         $.each(opts.rightData, function (key, val) {
        	 optionRight += '<option id=' + val.id + '>' + val.text + '</option>';
          });
          this.find('#pickListResult').append(optionRight);
      };
      this.controll = function () {
         var pickThis = this;

         $("#pAdd").on('click', function () {
            var p = pickThis.find("#pickData option:selected");
            p.clone().appendTo("#pickListResult");
            p.remove();
         });
         
         $("#pAddAll").on('click', function () {
            var p = pickThis.find("#pickData option");
            p.clone().appendTo("#pickListResult");
            p.remove();
         });

         $("#pRemove").on('click', function () {
            var p = pickThis.find("#pickListResult option:selected");
            p.clone().appendTo("#pickData");
            p.remove();
         });

         $("#pRemoveAll").on('click', function () {
            var p = pickThis.find("#pickListResult option");
            p.clone().appendTo("#pickData");
            p.remove();
         });
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
                 " <div><button id='pAdd' class='btn btn-primary btn-sm'>" + opts.add + "</button>" +
                 "      <button id='pAddAll' class='btn btn-primary btn-sm'>" + opts.addAll + "</button></div>" +
                 "	<button id='pRemove' class='btn btn-primary btn-sm'>" + opts.remove + "</button>" +
                 "	<button id='pRemoveAll' class='btn btn-primary btn-sm'>" + opts.removeAll + "</button>" +
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
      add: '添加',
      addAll: '添加所有',
      remove: '移除',
      removeAll: '移除所有'
   };


}(jQuery));


