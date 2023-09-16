package dongduk.cs.pulpul.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import dongduk.cs.pulpul.dao.OrderDao;
import dongduk.cs.pulpul.dao.mybatis.mapper.OrderMapper;
import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.domain.Order;

@Repository
public class MybatisOrderDao implements OrderDao {
	
	@Autowired
    private OrderMapper orderMapper;

	@Override
	public Cart findCartByMember(String memberId) throws DataAccessException {
		return orderMapper.selectCartByMemberId(memberId);
	}

	@Override
	public int findNumberOfCartItemByMember(String memberId) throws DataAccessException {
		return orderMapper.selectNumberOfCart(memberId);
	}
	
	@Override
	public boolean isExistItem(String memberId, String goodsId) throws DataAccessException {
		int ck = orderMapper.selectCartCountBygoodsId(memberId, goodsId);
		if (ck > 0) return true;
		return false;
	}

	@Override
	public void createCartItem(String memberId, CartItem cartItem) throws DataAccessException {
		orderMapper.insertCart(memberId, cartItem);
	}

	@Override
	public void deleteOneCartItem(String memberId, String itemId) throws DataAccessException {
		orderMapper.deleteOneCartByItemId(memberId, itemId);
	}

	@Override
	public int deleteCartItemByMarket(String memberId, int marketId) throws DataAccessException {
		return orderMapper.deleteCartByMarketId(memberId, marketId);
	}

	@Override
	public Order findOrder(int orderId) throws DataAccessException {
		return orderMapper.selectOrder(orderId);
	}

	@Override
	public List<Order> findOrderByMember(String memberId, String keyword) throws DataAccessException {
		return orderMapper.selectOrderByMemberId(memberId, keyword);
	}

	@Override
	public void createOrder(Order order) throws DataAccessException {
		orderMapper.insertOrder(order);
	}

	@Override
	public void createOrderGoods(List<Map<String, Object>> orderGoodsList) throws DataAccessException {
		orderMapper.insertOrderGoods(orderGoodsList);
	}

	@Override
	public void changeTrackingNumber(int orderId, String trackingNumber) throws DataAccessException {
		orderMapper.updateTrackingNumber(orderId, trackingNumber);
	}

	@Override
	public void changeOrderStatus(int orderId, int orderStatus) throws DataAccessException {
		orderMapper.updateOrderStatus(orderId, orderStatus);
	}

}
