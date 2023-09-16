/**
 * 
 */
 let url = window.location.href;
 
 if($('tbody').children().length == 1){ //주문 내역이 없을 때
    $("#totalPrice").text("0원");
    if(url.includes("trackingNumber")){ //해당 운송장 번호 검색시 주문 내역이 없는 경우
		Swal.fire({
					  text: "해당 주문내역이 없습니다.",
	    			  confirmButtonColor: '#93c0b5',
					  confirmButtonText: '확인',
					  }).then((result) => {
					  if (result.isConfirmed) { 
							location.href="/market/orderList";
					  }
				  	});
		
		 }
	 }
	
 function searchTrackingNumber(){
	let trackingNumber = $('.filterText').val();
	if(trackingNumber == ''){ //입력 안하고 검색했을 때
		Swal.fire({
				  text: "운송장 번호를 입력해주세요.",
    			  confirmButtonColor: '#93c0b5',
				  confirmButtonText: '확인',
				  }).then((result) => {
				  if (result.isConfirmed) { 
				  }
			  	});
			  	return;
	}
	
	const uri = '/market/orderList?trackingNumber=' + trackingNumber;
	encodeUri(uri);
}

