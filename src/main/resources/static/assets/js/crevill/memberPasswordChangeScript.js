var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      passwordAgain : '현재비밀번호',
			  newPassword : '신규비밀번호',
			  newPasswordAgain : '신규비밀번호재입력'
		    }
	  	}
  }
})

new Vue({
    el: '#appCapsule',
    data: {
    	passwordAgain : '',
  	    newPassword : '',
	    newPasswordAgain : ''
    },
	methods: {
    validateBeforeSubmit() {
      this.$validator.validate().then((result) => {
        if (result) {
	        
			acceessableCount  = acceessableCount - 1; //count부터 뺀다
			
			if (acceessableCount < 0 ) {
		    	alert("이미 작업이 수행중입니다.");
		    } else {
				var formdata = new FormData();
				formdata.append("password", $('#newPassword').val());
				
				axios.post(contextRoot + 'member/passwordChange.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
						location.href = contextRoot + 'member/info.view';
				    } else {
						alert('비밀번호 변경 중 오류가 발생했습니다. 다시 시도하여 주세요.');
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
			
			acceessableCount = acceessableCount + 1;
				
        } else {
			alert('항목을 올바르게 입력해주세요.');
		}
        
      });
    }
  }
})