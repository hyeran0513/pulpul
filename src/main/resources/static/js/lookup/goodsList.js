/**
 * 
 */

//태그로 검색
function searchTag(value){
	
		$(".tag-name:contains('"+value+"')").each(function() {
		  var $this = $(this);
		  $this.parents(".item").show();

		});
}

//검색 필터
jQuery(function($){ $('.filterText').keyup(function(event) { 
	var val = $(this).val(); 
 	if (val == "") {
	     $('.item').show();
	 }else {
		 $('.item').hide(); 
		
		//태그로 검색
		searchTag(val);
		
		//식물 이름으로 검색
		if($(".item-name:contains('"+val+"')")){
			console.log(val);
			$(".item-name:contains('"+val+"')").parents('.item').show();
		}
		
 	  }
   });
 });

//태그 클릭 시 이벤트 처리
function onClickTag(event){
	let tag = event.target.innerHTML;
	let val = $('input[id=itemSearch]').val(tag);
	
	if (tag == "") {
	     $('.item').show();
	 }else {
		 $('.item').hide(); 
	
	//태그로 검색
	 searchTag(tag);
		
	}
}

const tags = document.querySelectorAll('.search-tag');
tags.forEach( (tag) => {
  tag.addEventListener('click', onClickTag)
});
