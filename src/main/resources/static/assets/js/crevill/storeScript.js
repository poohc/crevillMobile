var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      storeType : '운영형태',
			  storeName : '매장명[간판]',
   			  storeNameReg : '매장명[사업자등록상]',		  
			  registrationNumber : '사업자등록번호',
			  telNo : '전화번호',
			  ceo : '대표자',
			  roadAddress : '도로명주소',
			  detailAddress : '상세주소',
			  openDay : '오픈일',
			  closeDay : '휴무일',
	 		  storePlay : '수업',
		    }
	  	}
  }
});

new Vue({
    el: '#appCapsule',
    data: {
    	storeType : '',
	  	storeName : '',
		storeNameReg : '',	  	
		registrationNumber : '',
		telNo : '',
	  	ceo : '',
	  	roadAddress : '',
		detailAddress : '',
	  	openDay : '',
		closeDay : '',
		storePlay : '',
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
				formdata.append("storeType", $('#storeType').val());
				formdata.append("storeName", $('#storeName').val());
				formdata.append("storeNameReg", $('#storeNameReg').val());
				formdata.append("registrationNumber", $('#registrationNumber').val());
				formdata.append("telNo", $('#telNo').val());
				formdata.append("ceo", $('#ceo').val());
				formdata.append("address", $('#roadAddress').val() + ' | ' + $('#detailAddress').val());
				formdata.append("openDay", $('#openDay').val());
				formdata.append("closeDay", $('#closeDay').val());
				formdata.append("experienceClass", $("input[name=experienceClass]:checked").val());
				
				var storePlay = '';
				$("input[name=storePlay]:checked").each(function() {
					storePlay += $(this).val() + ',';
				});
				storePlay = storePlay.substr(0, storePlay.length - 1);
				formdata.append("storePlay", storePlay);
				
				if($("#registrationCertificate")[0].files[0] != undefined){
					formdata.append("registrationCertificate", $("#registrationCertificate")[0].files[0]);	
				}
				
				if($("#playgroundCertificate")[0].files[0] != undefined){
					formdata.append("playgroundCertificate", $("#playgroundCertificate")[0].files[0]);	
				}
				
				if($("#insuranceCertificate")[0].files[0] != undefined){
					formdata.append("insuranceCertificate", $("#insuranceCertificate")[0].files[0]);	
				}
				
				if($("#etcFile1")[0].files[0] != undefined){
					formdata.append("etcFile1", $("#etcFile1")[0].files[0]);	
				}
				
				if($("#etcFile2")[0].files[0] != undefined){
					formdata.append("etcFile2", $("#etcFile2")[0].files[0]);	
				}
				
				if($("#etcFile3")[0].files[0] != undefined){
					formdata.append("etcFile3", $("#etcFile3")[0].files[0]);	
				}
				
				if($("#etcFile4")[0].files[0] != undefined){
					formdata.append("etcFile4", $("#etcFile4")[0].files[0]);	
				}
				
				if($("#thumbnailImage")[0].files[0] != undefined){
					formdata.append("thumbnailImage", $("#thumbnailImage")[0].files[0]);	
				}
				
				if($("#image1")[0].files[0] != undefined){
					formdata.append("image1", $("#image1")[0].files[0]);	
				}
				
				if($("#image2")[0].files[0] != undefined){
					formdata.append("image2", $("#image2")[0].files[0]);	
				}
				
				if($("#image3")[0].files[0] != undefined){
					formdata.append("image3", $("#image3")[0].files[0]);	
				}
				
				if($("#image4")[0].files[0] != undefined){
					formdata.append("image4", $("#image4")[0].files[0]);	
				}
							
				if($("#image5")[0].files[0] != undefined){
					formdata.append("image5", $("#image5")[0].files[0]);	
				}
				
				if($("#image6")[0].files[0] != undefined){
					formdata.append("image6", $("#image6")[0].files[0]);	
				}
				
				axios.post('/store/regist.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
						location.href = '/store/regist.view';
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
}); 

$('input[name="openDay"]').daterangepicker({
	singleDatePicker : true,
	locale: {
      format: 'YYYYMMDD',
      separator: '',
      applyLabel: "적용",
      cancelLabel: "닫기"
    } 	
});

function cancel(){
	location.href = '/store/regist.view';
}