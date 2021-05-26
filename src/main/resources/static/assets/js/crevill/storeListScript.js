 $("#storeName").on("keydown",function(key){
    if(key.keyCode==13) {
     	location.href = contextRoot + 'store/storeList.view?storeName=' + $('#storeName').val();   
    }
 });