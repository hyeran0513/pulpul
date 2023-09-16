function openPopup(){
  var url = "/lookup/alert";		// arg1, arg2 변수를 get방식으로 전송
  var title = "popup";
  var status = "toolbar=no,scrollbars=no,resizable=yes,status=no,menubar=no,width=260, height=120, top=0,left=0"; 
  
  window.open(url,title,status); 			// 팝업 open
  }