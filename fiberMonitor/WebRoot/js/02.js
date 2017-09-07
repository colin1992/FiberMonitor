/**
 * 
 */
$(document).ready(function(){
	$('#select-plays>li').addClass('horiz');//顶级菜单采用水平对齐
	$('#select-plays li:not(.horiz)').addClass('sub-level');
	$('a[href$=".jpg"]').addClass('imaglink');
	$('a[href$=".pdf"]').addClass('pdflink');
	 
});