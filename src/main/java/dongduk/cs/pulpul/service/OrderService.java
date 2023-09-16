package dongduk.cs.pulpul.service;

import java.util.List;

import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.domain.Order;
import dongduk.cs.pulpul.service.exception.AddCartException;
import dongduk.cs.pulpul.service.exception.CancelOrderException;
import dongduk.cs.pulpul.service.exception.OrderException;

public interface OrderService {
	
	/*
	 * 장바구니 관련 service method
	 */
	
	// 장바구니 조회
	Cart getCart(String memberId);
	
	// 회원의 장바구니 상품 수 조회
	int getNumberOfCartItemByMember(String memberId);
	
	// 장바구니에 상품 추가
	void addCartItem(String memberId, CartItem cartitem) throws AddCartException;
	
	// 장바구니에서 상품 삭제
	void deleteCartItem(String memberId, String itemId);
	
	// 장바구니에서 특정 마켓의 상품 목록 삭제
	int deleteCartItemByMarket(String memberId, int marketId);
	
	
	/*
	 * 주문 관련 service method
	 */
	
	// 회원 id로 주문 목록 조회
	List<Order> getOrderListByMember(String memberId, String identity);
	
	// 주문 상세내역 조회
	Order getOrder(int orderId);
	
	// 주문
	int order(Order order) throws OrderException;
	
	// 주문에 대한 운송장번호 입력
	void changeTrackingNumber(Order order);
	
	// 주문 취소
	void cancelOrder(int orderId) throws CancelOrderException;
	
	// 주문 확정
	void finalizeOrder(int orderId);
	
}
