/**
 * 
 */
//배송비 계산
 let totalItem = $("#total-item").text().replace(",", "").split("원")[0];
 let totalPrice = Number($("#totalPrice").text().replace(",", "").split("원")[0]);
 let shipping = $("#total-shippingFee");

let itemNum = $(".item-name");
	if(!$("#point")){
	shipping.text("+" + (totalPrice - totalItem).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원");
	}else{
		let point = -Number($("#point").text().replace(",", "").split("원")[0]);
		shipping.text("+" + (totalPrice - totalItem + point).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원");	
}