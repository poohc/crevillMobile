var listVm = new Vue({
    el: '#listDiv',
    data: {
			list : []
	},
	methods: {
	    	//정렬 처리(총 포인트 내림차순 정렬)
        	정렬처리 : function(arr) {
        		var 정렬된아이템 = arr.slice().sort(function(a, b) {
        			return b.scheduleStart - a.scheduleStart;
                });
        		return 정렬된아이템;
        	},
			검색 : function(scheduleType){
				
				if(scheduleType == 'ALL') $('#scheduleType').val('ALL');
				if(scheduleType == 'ING') $('#scheduleType').val('ING');
				if(scheduleType == 'END') $('#scheduleType').val('END');
				
				var formdata = new FormData();
					formdata.append("scheduleStart", $('#scheduleStart').val());
					formdata.append("scheduleType",  $('#scheduleType').val());
					
					axios.post('/reservation/getReservationList.proc', formdata,{
						  headers: {
							'Content-Type': 'multipart/form-data'
						  }
					}).then((response) => {
						  if(response.data.length == 0){
							  listVm.list.splice(0);
						  } else {
						  	  for(var i=0; i < response.data.length; i++){
								Vue.set(listVm.list, i, response.data[i]);
							  }	
							  listVm.list.splice(response.data.length);
						  }
						  
					}).catch(function (error) {
						console.log(error);
					});
			},
        },
}); 

$(function(){
	
	var formdata = new FormData();
		formdata.append("scheduleStart", $('#scheduleStart').val());
		formdata.append("scheduleType",  $('#scheduleType').val());
		
		axios.post('/reservation/getReservationList.proc', formdata,{
			  headers: {
				'Content-Type': 'multipart/form-data'
			  }
		}).then((response) => {
			  for(var i=0; i < response.data.length; i++){
				Vue.set(listVm.list, i, response.data[i]);
			  }	
			  listVm.list.splice(response.data.length);	
		}).catch(function (error) {
			console.log(error);
		});
		
});

$('input[name="scheduleStart"]').daterangepicker({
	singleDatePicker : true,
	locale: {
      format: 'YYYYMMDD',
      separator: '',
      applyLabel: "적용",
      cancelLabel: "닫기"
    } 	
});