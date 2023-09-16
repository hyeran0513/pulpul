 
/* 
페이지 첫 로드할 때 div와 table, checkbox에 class명 추가
체크할 때마다 가격 보여주기 위해 필요한 함수
 */
cartCheck();

function cartCheck(){
	
	var i = 0;
	
	$(".market-name").each(function(){

	let tableName = $(this).parent().find("table");
	tableName.attr('class', 'marketTable' + i); //cartItem들을 보여주는 Table에 class명 추가 _0, _1, _2..
	let divName = $(this).parent();
	divName.attr('class', 'marketDiv_' + i); //cartItemList들이 포함된 div에 class명 추가
	
	i++;
})
	
}
function moveToGoods(id, itemOpen, marketOpen){
	if(itemOpen == 0 || marketOpen == true){
		Swal.fire({
			      text: '판매중인 상품이 아닙니다.',
			      confirmButtonColor: '#93c0b5',
			      confirmButtonText: '확인',
			    })
		return;
	}else{
		moveToGoodsDetail(id);
	}
}
/*
 같은 마켓의 상품만 주문 가능
 <button class="check-item-order">선택 상품 주문</button>
 */
$(".check-item-order").each(function(){
	$(this).click(function(event){
		
		event.preventDefault();
		let sequence = $(this).parents("div")[1].classList[0].split("_")[1] //table class sequence
		console.log(sequence);
		let table = "marketTable" + sequence; 
	
		let checkLength = $("." + table  + " input[type=checkbox]:checked").length; //해당 마켓에서 선택한 상품 개수
		let marketChecks = $("table input[type=checkbox]:checked"); //모든 마켓들 중에서 check된 상품
		
		//not() 특정 선택자 제외, 주문버튼 누른 마켓에서 체크한 상품이 아닌 다른 마켓에 선택한 상품이 있는 경우 length > 0임.
		let otherMarketCheck = marketChecks.not("." + table  + " input[type=checkbox]:checked").length;
		
		if(otherMarketCheck > 0){ //다른 마켓의 상품도 선택했을 경우
			 Swal.fire({
			      text: '같은 마켓의 상품만 주문 가능합니다.',
			      confirmButtonColor: '#93c0b5',
			      confirmButtonText: '확인',
			    })
		}
	    else if(checkLength == 0){ //선택한 상품이 없는 경우
			 Swal.fire({
			      text: '해당 마켓에 선택하신 상품이 없습니다.',
			      confirmButtonColor: '#93c0b5',
			      confirmButtonText: '확인',
			    })
		}else{
			$("form").submit();
		}
	})
})

/*
마켓별로 담은 상품 보여주기 위해
마켓 이름이 같으면 해당 item을 마켓 이름이 같은 table에 append 후,
해당 item을 담고 있던 div 삭제 -> div class명 다시 정의 (class명 형태-div_0, div_1)
*/
let marketName = document.querySelectorAll(".market-name");
for(let k = 0; k < marketName.length; k++){
	for(let j = k + 1; j < marketName.length; j++){
		if(marketName[k].innerHTML == marketName[j].innerHTML){
			let cartItem = $(marketName[j]).next().children().find(".cartItem-wrap"); 
			$(cartItem).closest("div").remove();
			cartCheck();
			$(marketName[k]).next().children().append(cartItem);
		}	
	}
}

/*
 카트에 담긴 마켓 별로 총 상품 금액(price * quantity), 배송비(shipping) 담을 변수 생성
*/
let object = {};
for(var i = 0; i < document.querySelectorAll(".market-name").length; i++){
  object[`price${i}`] = 0; //마켓 수만큼 동적으로 변수 생성 price0, price1, price2..
  object[`shipping${i}`] = 0; 
}

/*
 체크할 때마다 상품 금액과 배송비 계산 - 총 결제 금액 보여주기 위해
*/
$("table input[type=checkbox]").click(function(){
	let div = $(this).parents("div")[0];
	let sequence = div.className.split("_")[1]; //div 번호로 해당 객체(object_[sequence]) 받아오기 위해 
	const total = $(this).parents("."+div.className).find(".total-price");
	
	let price = $(this).parent().siblings(".price").text();
	let shipping = $(this).parent().siblings(".shipping").text();
	let quantity = Number($(this).parent().siblings(".item-num").text());
	
	//체크 true
	if($(this).is(":checked") == true){
			object[`price${sequence}`] += Number(price) * quantity; //총 상품 금액 저장
			console.log(object[`shipping${sequence}`]);
			if(object[`shipping${sequence}`] < Number(shipping)){
				object[`shipping${sequence}`] = Number(shipping); //check 상품들 중 큰 배송비 저장
			}
	 }else{ //체크 false
			object[`price${sequence}`] -= Number(price) * quantity;
			let table = "marketTable" + sequence;
			let checkLength = $("." + table  + " input[type=checkbox]:checked").length; //해당 마켓에서 선택한 상품 개수
			
			if(checkLength == 0){ //선택한 상품 없을 때
				object[`shipping${sequence}`] = 0;
			}else{
				let arr = [];
				$("." + table  + " input[type=checkbox]:checked").each(function(){
					let checkShipping = $(this).parent().siblings(".shipping").text();
					arr.push(checkShipping);
				})
				let maxShipping = Math.max.apply(null, arr); //체크한 상품들 중 가장 큰 배송비 선택
				object[`shipping${sequence}`] = maxShipping;
			}
	}
	
	let totalPrice = object[`price${sequence}`] + object[`shipping${sequence}`]; //총 결제 금액
	$(this).parents("."+div.className).find(".total-price-item").text(object[`price${sequence}`].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
	$(this).parents("."+div.className).find(".total-shipping").text(object[`shipping${sequence}`].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
	total.text(totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
})

