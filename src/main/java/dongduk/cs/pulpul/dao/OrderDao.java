package dongduk.cs.pulpul.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.domain.Order;

public interface OrderDao {
	
	/*
	 * 장바구니 관련 dao method
	 */
	
	// 회원 장바구니 조회
	Cart findCartByMember(String memberId) throws DataAccessException;
	
	// 회원 장바구니 상품 수 조회
	int findNumberOfCartItemByMember(String memberId) throws DataAccessException;
	
	// 회원 장바구니에 특정 상품이 있는지 확인
	boolean isExistItem(String memberId, String goodsId) throws DataAccessException;
	
	// 장바구니 상품 생성
	void createCartItem(String memberId, CartItem cartItem) throws DataAccessException;
	
	// 상품 id로 장바구니 상품 삭제
	void deleteOneCartItem(String memberId, String itemId) throws DataAccessException;
	
	// 마켓 id로 장바구니 상품 목록 삭제
	int deleteCartItemByMarket(String memberId, int marketId) throws DataAccessException;
	
	
	/*
	 * 주문 관련 dao method
	 */
	
	// 주문 id로 주문 조회
	Order findOrder(int orderId) throws DataAccessException;
	
	// 회원 id로 주문 목록 조회
	List<Order> findOrderByMember(String memberId, String keyword) throws DataAccessException;
	
	// 주문 생성
	void createOrder(Order order) throws DataAccessException;
	
	// 주문 상품 목록 생성
	void createOrderGoods(List<Map<String, Object>> orderGoodsList) throws DataAccessException;
	
	// 주문 운송장번호 입력
	void changeTrackingNumber(int orderId, String trackingNumber) throws DataAccessException;
	
	// 주문 상태 변경
	void changeOrderStatus(int orderId, int orderStatus) throws DataAccessException;
	
}
