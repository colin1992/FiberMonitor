/**
 * 对所有的输入框进行限制
 */
function valide(id){
		var th=document.getElementById(id);	
		var wrongCharacter = /[<> %;!#$^&*)(&{}[]|\/]/g; //在[]中填写需要屏蔽的特殊字符
	    if(wrongCharacter.test(th.value)){
			$(th).val(th.value.replace(wrongCharacter,""));
	    }
	}
$(function(){	
	$("input[type='text']").attr('oninput','valide(this.id)');
	$("input[type='email']").attr('oninput','valide(this.id)');
	$("input[type='password']").attr('oninput','valide(this.id)');
	$("input[type='tel']").attr('oninput','valide(this.id)');
	$("textarea").attr('oninput',"valide(this.id)");
})