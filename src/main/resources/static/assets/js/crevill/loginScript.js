Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      cellPhone : '전화번호',
			  password : '비밀번호',
		    }
	  	}
  }
});

new Vue({
    el: '#appCapsule',
    data: {
    	cellPhone : '',
	  	password : '',
    },
	methods: {
    validateBeforeSubmit() {
      this.$validator.validate().then((result) => {
        if (result) {
	        
 			var formdata = new FormData();
			formdata.append("parentCellPhone", $('#cellPhone').val());
			formdata.append("password", $('#password').val());
			
			axios.post(contextRoot + 'login/login.proc', formdata,{
				  headers: {
					'Content-Type': 'multipart/form-data'
				  }
				}).then((response) => {
				if (response.data.resultCd == '00') {
			      	location.href = contextRoot + 'main.view';
			    } else {
					if (response.data.resultCd == '11') {
						alert('최초 비밀번호 설정을 위해 이동합니다.');
						location.href = contextRoot + "login/smsAuth.view?cellPhone=" + $('#cellPhone').val();	
					} else {
						alert(response.data.resultMsg);
						return false;	
					}
				}
				
			});	
        } else {
			alert('항목을 올바르게 입력해주세요.');
		}
        
      });
    }
  }
});