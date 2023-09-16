document.addEventListener("DOMContentLoaded", function() {    
	goodsJson(marketId); // 마켓 페이지로 처음 이동했을 때 goodsList json 정보를 가져옴
});

// goodsList의 json 정보를 비동기적으로 가져오는 함수
function goodsJson(marketId) {
	$("#goods-menu").addClass(' list-on');
	$("#shareThing-menu").removeClass('list-on');
	$.ajax({
			type: "get",
			url: "/lookup/market/goodsList/" + marketId,
			contentType: "application/json",
			processData: false,
			success: function(responseJson){	// responseJson: JS object parsed from JSON text			
			   	console.log(responseJson);
			   	// 이 부분에서 상품 리스트를 출력하는 코드를 짜야 함
			   	$("#itemWrap").each(function(){
					$(this).children(".item").remove();
				})
			   	 $.each(responseJson,function(index,goods){  
				   
				     /*
				  <div class="item" onclick="location.href='/lookup/goodsDetail'">
		          <div class="item-img-size"></div>
		          <div class="plant-name">### 식물</div>
		          <div class="plant-price">8000원</div>
		         
				  */
				    var divItem = document.createElement("div");
				    divItem.classList.add("item");
				    //onclick event
				    $(divItem).click(function(){
						moveToGoodsDetail(goods.item.id);
				  })
				  
				    var itemImg = document.createElement("div");
				    itemImg.classList.add("item-img-size");
				    var img = document.createElement("img");
				    img.src = goods.item.thumbnailUrl;
				    
				    if (goods.remainQuantity == 0) {
							img.classList.add("soldout-img");
						}
						else {
							img.classList.add("item-img");
						}
						
				    itemImg.appendChild(img);
				    
				    
				    var itemName = document.createElement("div");
				    itemName.classList.add("plant-name");
				    itemName.innerHTML += goods.item.name;
				    
				    if (goods.remainQuantity == 0) {
							itemName.innerHTML += '<span class="soldout">품절</span>';
						}
				    
				    var itemPrice = document.createElement("div");
				    itemPrice.classList.add("plant-price");
				    itemPrice.innerHTML = goods.price;
				   
				   divItem.appendChild(itemImg);
				   divItem.appendChild(itemName);
				   divItem.appendChild(itemPrice);
				   
				   $("#itemWrap").append(divItem);
				});
			   	
		    },
			error: function(){
				alert("ERROR", arguments);
			}
		});
}

// shareThingList의 json 정보를 비동기적으로 가져오는 함수
function shareThingJson(marketId) {
	$("#shareThing-menu").addClass(' list-on');
	$("#goods-menu").removeClass('list-on');
	$.ajax({
			type: "get",
			url: "/lookup/market/shareThingList/" + marketId,
			contentType: "application/json",
			processData: false,
			success: function(responseJson){	// responseJson: JS object parsed from JSON text			
			   console.log(responseJson);
			   	// 이 부분에서 공유물품 리스트를 출력하는 코드를 짜야 함
			   	$("#itemWrap").each(function(){
					$(this).children(".item").remove();
				})
			   	 $.each(responseJson,function(index,shareThing){   
				     /*
				  <div class="item" onclick="location.href='/lookup/shareThingDetail'">
	        	<div class="item-img-size"></div>
	          <div class="itemWaitingWrap">
		          <div class="item-name">### 물품</div>
		
		          <div class="waitingWrap">
		            <img src="/images/waiting.svg" />
		            <div class="waiting">2</div>
		          </div>
	             </div>
	
	          <div class="rental-ok">대여 가능</div>
	          </div>
		         
				  */
				  console.log(shareThing.item.name);
				    var divItem = document.createElement("div");
				    divItem.classList.add("item");
				    //onclick event
				    $(divItem).click(function(){
						moveToShareThingDetail(shareThing.item.id);
				  })
				  
				    var itemImg = document.createElement("div");
				    itemImg.classList.add("item-img-size");
				    var img = document.createElement("img");
				    img.src = shareThing.item.thumbnailUrl;
				    img.classList.add("item-img");
				    itemImg.appendChild(img);
				    
				    var itemWaitingWrap = document.createElement("div");
				    itemWaitingWrap.classList.add("itemWaitingWrap");
				   
				    var itemName = document.createElement("div"); 
				    itemName.classList.add("item-name");
				    itemName.innerHTML = shareThing.item.name;
				    
				    var waitingWrap = document.createElement("div");
				    waitingWrap.classList.add("waitingWrap");
				    var waitingImg = document.createElement("img");
				    waitingImg.src = "/images/waiting.svg";
				    var waiting = document.createElement("div");
				    waiting.classList.add("waiting");
				    waiting.innerHTML = shareThing.reservationNumber;
				    
				    waitingWrap.appendChild(waitingImg);
				    waitingWrap.appendChild(waiting);
				    
				   itemWaitingWrap.appendChild(itemName);
				   itemWaitingWrap.appendChild(waitingWrap);
				   
				   var rental = document.createElement("div");
				   if(shareThing.isBorrowed == 1) {
						rental.classList.add("rental-notok");
						rental.innerHTML = "대여 불가";
						} else {
							rental.classList.add("rental-ok");
							rental.innerHTML = "대여 가능";
						}
					
				   divItem.appendChild(itemImg);
				   divItem.appendChild(itemWaitingWrap);
				   divItem.appendChild(rental);
				   
				   $("#itemWrap").append(divItem);
				});
			   	
		    },
			error: function(){
				alert("ERROR", arguments);
			}
		});
}