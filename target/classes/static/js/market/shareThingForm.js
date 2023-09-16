/**
 * 
 */
 $("#saveBtn").click(function(event){
	event.preventDefault();
	 if($("#putItemImg").children().length == 0){
		 Swal.fire({
				   text: '공유물품 이미지를 등록해주세요!',
				   confirmButtonColor: '#93c0b5',
				   confirmButtonText: '확인',
				});
	}else{
		$('form').submit();
	}
})