function cancel(reservationId, voucherUseId, isToday){
	
	if(confirm('예약을 취소처리 하시겠습니까?')){
		
		if(isToday == 'TODAY'){
			alert('당일 취소는 불가능합니다.');
			return false;
		} else {
			var formdata = new FormData();
			formdata.append("reservationId", reservationId);
			formdata.append("voucherUseId", voucherUseId);
			
			axios.post(contextRoot + 'reservation/cancel.proc', formdata,{
				  headers: {
					'Content-Type': 'multipart/form-data'
				  }
				}).then((response) => {
				if (response.data.resultCd == '00') {
			      	alert('정상처리 되었습니다.');
					location.href = contextRoot + 'reservation/list.view';
			    } else {
					alert(response.data.resultMsg);
					return false;
				}
			}).catch(function (error) {
			    if (error.response) {
			      alert('처리 중 오류가 발생했습니다. 관리자에게 문의하여 주세요.');
			    }
			    else if (error.request) {
			      alert('처리 중 오류가 발생했습니다. 관리자에게 문의하여 주세요.');
			    }
			    else {
			      alert('처리 중 오류가 발생했습니다. 관리자에게 문의하여 주세요.');
			    }
		    });		
		}
		
	}
	
}