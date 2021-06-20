var timer;
var isRunning = false;

$('#sendAuthNum').click(function(){
	
	var formdata = new FormData();
		formdata.append("cellPhone", $('#cellPhone').val());
		formdata.append("password", $('#newPassword').val());
		
		axios.post(contextRoot + 'login/getAuthNum.proc', formdata,{
			  headers: {
				'Content-Type': 'multipart/form-data'
			  }
			}).then((response) => {
			if (response.data.resultCd == '00') {
				sendAuthNum();
				$('#sendAuthNumText').html('인증번호 재발송');
				$('#checkAuthNum').attr('disabled', false);
//				$('#sendAuthNum').attr('disabled', true);
	
		    } else {
				alert('인증번호 발송 중 오류가 발생했습니다. 다시 시도하여 주세요.');
				return false;
			}
		});
});

$('#checkAuthNum').click(function(){
	
	var formdata = new FormData();
		formdata.append("authNum", $('#authNum').val());
		
		axios.post(contextRoot + 'login/checkAuthNum.proc', formdata,{
			  headers: {
				'Content-Type': 'multipart/form-data'
			  }
			}).then((response) => {
			if (response.data.resultCd == '00') {
				alert('인증번호 확인이 완료되었습니다.');
				location.href= contextRoot + "login/passwordInit.view?cellPhone=" + $('#cellPhone').val();
	
		    } else {
				alert('인증번호 확인 중 오류가 발생했습니다. 다시 시도하여 주세요.');
				return false;
			}
		});
});


// 인증번호 발송하고 타이머 함수 실행
function sendAuthNum() {
	// 남은 시간
	var leftSec = 180,
		display = document.querySelector('#timer');
	// 이미 타이머가 작동중이면 중지
	if (isRunning) {
		clearInterval(timer);
	}
	startTimer(leftSec, display);
}

function startTimer(count, display) {
	var minutes, seconds;
	timer = setInterval(function() {
		minutes = parseInt(count / 60, 10);
		seconds = parseInt(count % 60, 10);
		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;
		display.textContent = minutes + ":" + seconds;
		// 타이머 끝
		if (--count < 0) {
			clearInterval(timer);
			display.textContent = "";
			isRunning = false;
		}
	}, 1000);
}