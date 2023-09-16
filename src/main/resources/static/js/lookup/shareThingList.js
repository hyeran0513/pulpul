
// 검색필터
$(document).ready(function(){
  $("#itemSearch").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    
    $(".item").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});