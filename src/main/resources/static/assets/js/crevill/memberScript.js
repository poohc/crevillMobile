var acceessableCount = 1; //동시접근제한수

const checkMemberCellPhone = (value) => {
  	return axios.post(contextRoot + 'member/checkMemberCellPhone.proc', {parentCellPhone: value}).then((response) => {
		if (response.data.resultCd == '00') {
	      return true;
	    }
		
		return {
	      data: {
	        message: '이미 가입되어 있는 번호입니다.'
	      }
	    };
	});
};

VeeValidate.Validator.extend('chkCellPhone', {
    validate : checkMemberCellPhone,
    getMessage: (field, params, data) => {
    	return data.message;
  	}
});

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      parentName : '고객성함',
			  email : '이메일',
			  password : '비밀번호',
			  passwordAgain : '비밀번호 재입력',
			  address : '주소',
			  detailAddress : '상세주소',	
			  cellPhone : '전화번호',
			  childName : '아동이름',
			  engName : '영어이름',
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
		password : '',
		passwordAgain : '',
	  	address : '',
		detailAddress : '',
	  	cellPhone : '',
	  	childName : '',
		engName : '',
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
				
	  			var learningGrade = '';
				$("input[name=learningGrade]:checked").each(function() {
					learningGrade += $(this).val() + ',';
				});
				learningGrade = learningGrade.substr(0, learningGrade.length - 1);
				
				var formdata = new FormData();
				formdata.append("parentName", $('#parentName').val());
				formdata.append("email", $('#email').val());
				formdata.append("password", $('#password').val());
				formdata.append("address", $('#address').val() + ' | ' + $('#detailAddress').val());
				formdata.append("cellPhone", $('#cellPhone').val());
				formdata.append("childName", $('#childName').val());
				formdata.append("engName", $('#engName').val());
				formdata.append("birthday", $('#birthday').val());
				formdata.append("sex", $('#sex').val());
				formdata.append("learningGrade", learningGrade);
				
				if($("#camera")[0].files[0] != undefined){
					formdata.append("picture", $("#camera")[0].files[0]);	
				}
				
				axios.post(contextRoot + 'member/join.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
						location.href = contextRoot;
				    }
					
					return {
				      data: {
				        message: '이미 가입되어 있는 번호입니다.'
				      }
				    };
					
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


$('input[name="birthday"]').daterangepicker({
	singleDatePicker : true,
	locale: {
      format: 'YYYYMMDD',
      separator: '',
      applyLabel: "적용",
      cancelLabel: "닫기"
    } 	
}); 

 $(function(){
    $('#camera').change(function(e){
        $('#picture').attr('src', URL.createObjectURL(e.target.files[0]));
    });
});

window.name ="Parent_window";
	
function fnPopup(){
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.form_chk.target = "popupChk";
	document.form_chk.submit();
}