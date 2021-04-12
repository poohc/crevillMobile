var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      storeId : '체험희망지점',
			  childName : '아동이름',
			  scheduleId : '튜터링',
   			  termsAgree : '개인정보의 수집 및 이용동의'
		    }
	  	}
  }
});

var vm = new Vue({
    el: '#appCapsule',
    data: {
    	storeId : '',
	    childName : '',
	    scheduleId : '',
	    termsAgree : '',
		scheduleList: []
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
				formdata.append("storeId", $('#storeId').val());
				formdata.append("childName", $('#childName').val());
				formdata.append("scheduleId", $('#scheduleId').val());
				
				axios.post(contextRoot + 'reservation/freeRegist.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('체험신청이 완료 되었습니다.\n 사정상 방문이 취소되거나 미리 예약취소를 부탁드립니다. \n 매장 사정상 무료체험이 사전종료 될 수 있는 점 양해구합니다.');
						location.href = contextRoot + 'reservation/list.view';
				    } else {
						alert(response.data.resultMsg);
					}
					
				});	
			}
			
 			acceessableCount = acceessableCount + 1;
		
        } else {
			alert('항목을 올바르게 입력해주세요.');
		}
        
      });
    },
  }
}); 

$('#scheduleId').click(function(){
	
	$.ajax({
		type : "POST",
		data: {
	            scheduleType : 'ING',
				classType : 'CLASS_D',
				experienceClass : 'Y'
	    },
		url : contextRoot + 'schedule/getScheduleList.proc',
		success : function(data){
			if(data.resultCd == '00'){
				for(var i=0; i < data.scheduleList.length; i++){
					Vue.set(vm.scheduleList, i, data.scheduleList[i]);
				} 
				vm.scheduleList.slice().sort(function(a, b) {
	    			return b.scheduleStart - a.scheduleStart;
	            });
			} else {
				alert('해당 날짜에 등록된 수업이 없습니다.');
				return false;	
			}
			
		},
		error : function(error) {
	        alert("수업 목록을 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});
	
});