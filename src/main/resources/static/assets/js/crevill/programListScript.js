 $("#storeName").on("keydown",function(key){
    if(key.keyCode==13) {
     	location.href = contextRoot + 'program/timeTable.view?storeName=' + $('#storeName').val();   
    }
 });