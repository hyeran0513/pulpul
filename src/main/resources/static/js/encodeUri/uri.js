/**
 * 
 */
 /* 경로 */
 function encodeUri(uri){
	const encoded = encodeURI(uri);
	location.href = encoded;
}

/******************************************************************************************** 
 /cart/cart.html 
********************************************************************************************/
 function deleteItem(itemId){
	const uri = '/cart/deleteItem?itemId=' + itemId;
	encodeUri(uri);
}

function deleteItemByMarket(marketId){
	location.href='/cart/deleteItemByMarket?marketId=' + marketId;
}

/******************************************************************************************** 
/market/goodsList.html, /lookup/goodsDetail.html, /lookup/goodsList.html ,
/market/orderDetail.html, /order/orderDetail.js
********************************************************************************************/
function updateGoods(id){
	const uri = '/market/goods/update?itemId=' + id;
	encodeUri(uri);
}

function deleteGoods(id){
	const uri = '/market/goods/delete?itemId=' + id;
	encodeUri(uri);
}

function moveToGoodsDetail(id){
	const uri = '/lookup/goodsDetail?itemId=' + id;
	encodeUri(uri);
}

/********************************************************************************************
/market/shareThing.html , /lookup/market.html, /market/shareThingList.html
********************************************************************************************/
function findBorrowShareThing(id){ //대여 현황
	const uri = '/market/shareThing/borrowList?itemId=' + id;
	encodeUri(uri);
}

function updateShareThing(id){
	const uri = '/market/shareThing/update?itemId=' + id;
	encodeUri(uri);
}

function deleteShareThing(id){
	const uri = '/market/shareThing/delete?itemId=' + id;
	encodeUri(uri);
}

function moveToShareThingDetail(id){
	const uri = '/lookup/shareThingDetail?itemId=' + id;
	encodeUri(uri);
}

/********************************************************************************************
/market/shareThingBorrowList.html 
********************************************************************************************/
function returnShareThing(id){
	const uri = '/borrow/return?borrowId=' + id;
	encodeUri(uri);
}

/********************************************************************************************
/lookup/goodsDetail.html 
********************************************************************************************/
function uploadReview(orderId, itemId){
	const uri = '/review?orderId=' + orderId + '&itemId=' + itemId;
	encodeUri(uri);
}

