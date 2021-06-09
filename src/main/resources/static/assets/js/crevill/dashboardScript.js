var listVm = new Vue({
    el: '#dashboardDiv',
    data: {
			list : []
	},
	methods: {
	    	예약검색 : function(event){
				
				var formdata = new FormData();
					formdata.append("reservationTime", event.target.value.replace('시',''));
					
					axios.post('/main/getTodayReservationInfo.proc', formdata,{
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
		formdata.append("reservationTime", $('#reservationTime').val().replace('시',''));
		
		axios.post('/main/getTodayReservationInfo.proc', formdata,{
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