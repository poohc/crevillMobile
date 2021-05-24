var acceessableCount = 1; //동시접근제한수

Vue.use(VeeValidate, {
  locale: 'ko',
  dictionary: {
    ko: {
		    attributes: {
		      childName : '아동이름',
			  voucherNo : '바우처'
		    }
	  	}
  }
});

var vm = new Vue({
    el: '#appCapsule',
    data: {
    	scheduleList: [],
        scheduleId : '',
		scheduleStart : '',
		playName : '',
		reservationCnt : ''
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
				formdata.append("voucherNo", $('#voucherNo').val());
				formdata.append("scheduleId", $('#scheduleId').val());
				formdata.append("childName", $('#childName').val());
				formdata.append("cellPhone", $('#cellPhone').val());
				
				axios.post(contextRoot + 'reservation/regist.proc', formdata,{
					  headers: {
						'Content-Type': 'multipart/form-data'
					  }
					}).then((response) => {
					if (response.data.resultCd == '00') {
				      	alert('정상처리 되었습니다.');
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

setTimeout(() => {
	notification('notification-welcome', 1500);
}, 2000);

$('#circle1').circleProgress({
	value: 0.2,
	size: 100,
	fill: {
		gradient: ["#1E74FD", "#592BCA"]
	},
	animation: {
		duration: 4000
	}
});
$('#circle2').circleProgress({
	value: 0.8,
	size: 100,
	fill: {
		gradient: ["#EC4433", "#FE9500"]
	},
	animation: {
		duration: 4000
	}
});
$('#circle3').circleProgress({
	value: 0.55,
	size: 500,
	fill: {
		gradient: ["#00CDFF", "#1E74FD"]
	},
	animation: {
		duration: 4000
	}
});

function reservation(scheduleId, scheduleStart, playName, reservationCnt){
	vm.scheduleId = scheduleId;
	vm.scheduleStart = scheduleStart;
	vm.playName = playName;
	vm.reservationCnt = reservationCnt;
	
	$.ajax({
		type : "POST",
		data: {
	            scheduleId : scheduleId
	    },
		url : contextRoot + 'reservation/getQuickReservationInfo.proc',
		success : function(data){
			if(data.resultCd == '00'){
				for(var i=0; i < data.scheduleList.length; i++){
					Vue.set(vm.scheduleList, i, data.scheduleList[i]);
				} 
				vm.scheduleList.slice().sort(function(a, b) {
	    			return b.scheduleStart - a.scheduleStart;
	            });
			} else {
				alert('해당 날짜에 예약 가능한 스케쥴이 없습니다.');
				return false;	
			}
			
		},
		error : function(error) {
	        alert("예약 목록을 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});	
}