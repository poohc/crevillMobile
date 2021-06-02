var acceessableCount = 1; //동시접근제한수
var calendarEvent = [{}];

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      cellPhone : '전화번호',
			  voucherNo : '바우처',
			  classType : '클래스/튜터링선택',
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
		voucherList : [],
		voucherTimeLeftHour : '',
		voucherEndDate : '',
		reservationCnt : '',
		classType : ''
    },
	methods: {
    validateBeforeSubmit() {
      this.$validator.validate().then((result) => {
        if (result) {
	        
			acceessableCount  = acceessableCount - 1; //count부터 뺀다
			
			if (acceessableCount < 0 ) {
		    	alert("이미 작업이 수행중입니다.");
		    } else {
			
				var scheduleId = '';
				$("input[name=scheduleId]:checked").each(function() {
					scheduleId += $(this).val() + ',';
				});
				scheduleId = scheduleId.substr(0, scheduleId.length - 1);
				
				var childName = '';
				$("input[name=childName]:checked").each(function() {
					childName += $(this).val() + ',';
				});
				childName = childName.substr(0, childName.length - 1);
				
				var formdata = new FormData();
				formdata.append("cellPhone", $('#cellPhone').val());
				formdata.append("voucherNo", $("input[name=voucherNo]:checked").val());
				formdata.append("scheduleId", scheduleId);
				formdata.append("childName", childName);
				formdata.append("classType", 'CLASS_A');
				formdata.append("experienceClass", 'Y');
				
				axios.post(contextRoot + 'reservation/regist.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	$('#DialogReserveSuccess').modal('show');
//						location.href = contextRoot + 'reservation/list.view';
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

$('input[name="voucherNo"]').click(function(){
	
	$.ajax({
		type : "POST",
		data: {
	            voucherNo : $(this).val()
	    },
		url : contextRoot + 'voucher/getVoucherInfo.proc',
		success : function(data){
			if(data.resultCd == '00'){
				vm.voucherTimeLeftHour = data.voucherInfo.timeLeftHour;
				vm.voucherEndDate = data.voucherInfo.endDate;
			} else {
				alert('바우처 정보를 불러올 수가 없습니다.');
				return false;	
			}
			
		},
		error : function(error) {
	        alert("바우처 정보를 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});
	
});

$('input[name="childName"]').click(function(){
	vm.reservationCnt = $("input:checkbox[name=childName]:checked").length;
});

$('#cal').click(function(){
	setCalEvent();
});

setTimeout(function() {
//강제이벤트 발생
$('input[name="voucherNo"]').trigger('click');
$('input[name="childName"]').trigger('click');
vm.classType = '클래스';
setReservationCalendar();
}, 1000);

function setReservationCalendar(){
	
	var tutoringYn = 'N';
	var operationType = 'WEEKDAY';
	
	$.ajax({
		type : "POST",
		data: {
	            tutoringYn : tutoringYn,
				operationType : operationType,
				experienceClass : 'Y'
	    },
		url : contextRoot + 'reservation/getAvaReservationList.proc',
		success : function(data){
			if(data.resultCd == '00'){
				
				for(var i=0; i < data.list.length; i++){
					var temp = {
					  date: data.list[i].scheduleStart
					}
					calendarEvent.push(temp);
				}
			} else {
//				alert('해당 조건으로 예약 가능한 날짜가 없습니다.');
				return false;	
			}
			
		},
		error : function(error) {
	        alert("예약 정보를 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});
}

function getReservationSearchList(scheduleStart){
	$.ajax({
		type : "POST",
		data: {
	            scheduleStart : scheduleStart,
				experienceClass : 'Y'
	    },
		url : contextRoot + 'reservation/getSearchDayReservation.proc',
		success : function(data){
			if(data.resultCd == '00'){
				
				for(var i=0; i < data.list.length; i++){
					Vue.set(vm.scheduleList, i, data.list[i]);
				} 
				vm.scheduleList.slice().sort(function(a, b) {
	    			return b.scheduleStart - a.scheduleStart;
	            });
			} else {
//				alert('해당 조건으로 예약 가능한 날짜가 없습니다.');
				return false;	
			}
			
		},
		error : function(error) {
	        alert("예약 정보를 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});
}

function setCalEvent(){
	$("#calendar").MEC({
		events: calendarEvent
	});
}

function registProc(){
	$('#reservationForm').submit();
}