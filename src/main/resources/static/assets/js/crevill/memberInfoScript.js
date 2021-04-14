function deleteMemberInfo(qrCode, cellPhone){
	if(confirm('해당 회원을 탈퇴 처리하시겠습니까?')){
		$.ajax({
			type : "POST",
			data: {
		            qrCode : qrCode,
					cellPhone : cellPhone.replace(/[^0-9]/g,"")
		    },
			url : '/member/delete.proc',
			success : function(data){
				if(data.resultCd == '00'){
					alert('탈퇴 처리되었습니다.');
					location.href = '/member/list.view';
				} else {
					alert('회원탈퇴 처리 중 오류가 발생했습니다. 다시 시도하여 주세요.');
					return false;	
				}
				
			},
			error : function(error) {
		        alert("회원탈퇴 처리 중 오류가 발생했습니다. 다시 시도하여 주세요.");
				return false;
		    }
		});	
	}
}

function updateMemberInfo(qrCode){
	var url = contextRoot + 'member/update.view?qrCode=' + qrCode;
	location.href = url;
}