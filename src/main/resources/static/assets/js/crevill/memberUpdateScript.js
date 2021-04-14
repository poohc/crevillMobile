var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      parentName : '고객성함',
			  email : '이메일',
			  address : '주소',
			  cellPhone : '전화번호',
			  childName : '아동이름',
			  birthday : '생년월일',
			  sex : '성별',
			  learningGrade : '영어학습수준'
		    }
	  	}
  }
})

new Vue({
    el: '#appCapsule',
    data: {
    	parentName : '',
	  	email : '',
	  	address : '',
	  	cellPhone : '',
	  	childName : '',
	  	birthday : '',
	  	sex : '',
	  	learningGrade : '',
    },
	methods: {
    validateBeforeSubmit() {
      this.$validator.validate().then((result) => {
        if (result) {
	        
			acceessableCount  = acceessableCount - 1; //count부터 뺀다
			
			if (acceessableCount < 0 ) {
		    	alert("이미 작업이 수행중입니다.");
		    } else {
			
				var address = $('#address').val() + '|' + $('#detailAddress').val(); 
			
				axios.post(contextRoot + 'member/update.proc', {
									            parentName : $('#parentName').val(),
									            email : $('#email').val(),
									            address : address,
									            qrCode : $('#qrCode').val()
			        }).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
						location.href = contextRoot + 'member/info.view';
				    } else {
						alert('업데이트 중 오류가 발생했습니다. 다시 시도하여 주세요.');
						return false;
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