var listVm = new Vue({
    el: '#appCapsule',
    data: {
			list : []
	}
}); 

$(function(){
	
//	var formdata = new FormData();
//		formdata.append("searchMonth", 'thisMonth');
//		formdata.append("storeId", $('#storeId').val());
//		
//		axios.post(contextRoot + 'program/getTimeTableList.proc', formdata,{
//			  headers: {
//				'Content-Type': 'multipart/form-data'
//			  }
//		}).then((response) => {
//			  for(var i=0; i < response.data.length; i++){
//				Vue.set(listVm.list, i, response.data[i]);
//			  }	
//			  listVm.list.splice(response.data.length);	
//		}).catch(function (error) {
//			console.log(error);
//		});
	
	if($('#tab').val() == 'thisMonth'){
		$('#thismonth').addClass('active');
		$('#thismonth').addClass('show');
		$('#navThisMonth').addClass('active');
		$('#nextmonth').removeClass('active');
		$('#nextmonth').removeClass('show');
		$('#navNextMonth').removeClass('active');
	} else if($('#tab').val() == 'nextMonth'){
		$('#thismonth').removeClass('active');
		$('#thismonth').removeClass('show');
		$('#navThisMonth').removeClass('active');
		$('#nextmonth').addClass('active');
		$('#nextmonth').addClass('show');
		$('#navNextMonth').addClass('active');
	}
		
	$('input[name="playCheckBoxThisMonth"]').change(function(){
		var playNameListArray = new Array(); 
		var playNameListNextArray = new Array(); 
		$("input[name=playCheckBoxThisMonth]:checked").each(function() {
			playNameListArray.push($(this).val());
		});
		$("input[name=playCheckBoxNextMonth]:checked").each(function() {
			playNameListNextArray.push($(this).val());
		});
		location.href = contextRoot + 'program/timeTableDetail.view?storeId=' + $('#storeId').val() + '&playNameList=' + playNameListArray + '&playNameListNext=' + playNameListNextArray + '&tab=thisMonth';
	});	
	
	$('input[name="playCheckBoxNextMonth"]').change(function(){
		var playNameListArray = new Array(); 
		var playNameListNextArray = new Array(); 
		$("input[name=playCheckBoxThisMonth]:checked").each(function() {
			playNameListArray.push($(this).val());
		});
		$("input[name=playCheckBoxNextMonth]:checked").each(function() {
			playNameListNextArray.push($(this).val());
		});
		location.href = contextRoot + 'program/timeTableDetail.view?storeId=' + $('#storeId').val() + '&playNameList=' + playNameListArray + '&playNameListNext=' + playNameListNextArray + '&tab=nextMonth';
	});	
		
});