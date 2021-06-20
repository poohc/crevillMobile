Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      newPassword : '신규비밀번호',
			  newPasswordAgain : '신규비밀번호 확인'
		    }
	  	}
  }
});

new Vue({
    el: '#appCapsule',
    data: {
    	password : '',
    },
	methods: {
    validateBeforeSubmit() {
      this.$validator.validate().then((result) => {
        if (result) {
 			var formdata = new FormData();
			formdata.append("cellPhone", $('#cellPhone').val());
			formdata.append("password", $('#newPassword').val());
			
			axios.post(contextRoot + 'login/passwordInit.proc', formdata,{
				  headers: {
					'Content-Type': 'multipart/form-data'
				  }
				}).then((response) => {
				if (response.data.resultCd == '00') {
			      	alert('비밀번호가 정상적으로 설정되었습니다.');
					location.href = contextRoot + 'main.view';
			    } else {
					alert('비밀번호 설정 중 오류가 발생했습니다. 다시 시도하여 주세요.');
				}
			});	
        } else {
			alert('항목을 올바르게 입력해주세요.');
		}
        
      });
    }
  }
});