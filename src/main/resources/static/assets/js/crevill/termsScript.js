function nextProcess(){
	var terms1 = $("input[name=terms1]:checked").val();
	var terms2 = $("input[name=terms2]:checked").val();
	var terms3 = $("input[name=terms3]:checked").val();
				
	if(typeof terms1 == "undefined" || terms1 == null || terms1 == ""){
		terms1 = 'N';
	}
	
	if(typeof terms2 == "undefined" || terms2 == null || terms2 == ""){
		terms2 = 'N';
	}
	
	if(typeof terms3 == "undefined" || terms3 == null || terms3 == ""){
		terms3 = 'N';
	}
	
	if(terms1 != 'Y'){
		alert('이용약관에 동의하여 주세요.');
		return false;
	}
	
	if(terms2 != 'Y'){
		alert('개인정보취급방침에 동의하여 주세요.');
		return false;
	}
	
	var url = contextRoot + "member/join.view?terms1="+terms1+"&terms2="+terms2+"&terms3="+terms3
	location.href = url;
}