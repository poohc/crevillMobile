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
    	playList : [],
    }
}); 

$(document).ready(function(){
	getPlayList();
});



function getPlayList(){
	
	$.ajax({
		type : "POST",
		url : contextRoot + 'play/playList.view',
		success : function(data){
			for(var i=0; i < data.length; i++){
				Vue.set(vm.playList, i, data[i]);
			} 
			vm.playList.splice(data.length);
			
		},
		error : function(error) {
	        alert("플레이 목록을 가져오는 중에 오류가 발생했습니다. 다시 시도하여 주세요.");
			return false;
	    }
	});	
}