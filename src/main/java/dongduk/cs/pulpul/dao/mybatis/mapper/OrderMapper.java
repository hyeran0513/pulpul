package dongduk.cs.pulpul.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.domain.Order;

@Mapper
public interface OrderMapper {
	
	Cart selectCartByMemberId(String memberId);
	
	int selectNumberOfCart(String memberId);
	
	int selectCartCountBygoodsId(String memberId, String goodsId);
	
	void insertCart(@Param("memberId") String memberId,
			@Param("cartItem") CartItem cartItem);
	
	void deleteOneCartByItemId(@Param("memberId") String memberId, 
			@Param("itemId") String itemId);
	
	int deleteCartByMarketId(@Param("memberId") String memberId,
			@Param("marketId") int marketId);
	
	Order selectOrder(int orderId);
	
	List<Order> selectOrderByMemberId(String memberId, String keyword);
	
	void insertOrder(Order order);
	
	void insertOrderGoods(List<Map<String, Object>> orderGoods);
	
	void updateTrackingNumber(@Param("orderId") int orderId, 
			@Param("trackingNumber") String trackingNumber);
	
	void updateOrderStatus(@Param("orderId") int orderId, 
			@Param("orderStatus") int orderStatus);
	
}
