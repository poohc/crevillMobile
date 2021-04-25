var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      cellPhone : '전화번호',
			  voucherNo : '바우처',
   			  scheduleDate : '스케쥴날짜',		  
			  scheduleTime : '스케쥴시간',
			  scheduleId : '수업'
		    }
	  	}
  }
});

var vm = new Vue({
    el: '#appCapsule',
    data: {
    	cellPhone : '',
	  	voucherNo : '',
		storeId : '',
		scheduleDate : '',	  	
		scheduleTime : '',
	  	scheduleId : '',
		scheduleList: [],
		voucherList : []
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
				formdata.append("cellPhone", $('#cellPhone').val());
				formdata.append("voucherNo", $('#voucherNo').val());
				formdata.append("scheduleId", $('#scheduleId').val());
				formdata.append("classType", $('#classType').val());
				
				axios.post(contextRoot + 'reservation/regist.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
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
	            scheduleStart : $('#scheduleStart').val().replace(/[^0-9]/g,""),
				classType : $('#classType').val(),
				storeId : $('#storeId').val()
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

$('#storeId').change(function(){
	$('#storeNameSpan').text($("#storeId option:checked").text().split(" ")[($("#storeId option:checked").text().split(" ").length - 1)]);
});

$('#scheduleId').change(function(){
	$('#scheduleTimeSpan').text($('#scheduleId option:checked').attr('class'));
});

$('#storeNameSpan').text($("#storeId option:checked").text().split(" ")[($("#storeId option:checked").text().split(" ").length - 1)]);