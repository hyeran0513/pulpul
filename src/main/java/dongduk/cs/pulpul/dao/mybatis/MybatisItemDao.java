package dongduk.cs.pulpul.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import dongduk.cs.pulpul.dao.ItemDao;
import dongduk.cs.pulpul.dao.mybatis.mapper.ItemMapper;
import dongduk.cs.pulpul.domain.Goods;
import dongduk.cs.pulpul.domain.Item;
import dongduk.cs.pulpul.domain.ShareThing;

@Repository
public class MybatisItemDao implements ItemDao {

	@Autowired
    private ItemMapper itemMapper;
	
	@Override
	public void createItemImages(List<String> imageUrlList, String memberId) throws DataAccessException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberId", memberId);
		param.put("imageUrlList", imageUrlList);
		itemMapper.insertItemImages(param);
	}

	@Override
	public int deleteItemImages(String itemId, String memberId) throws DataAccessException {
		return itemMapper.deleteItemImages(memberId, itemId);
	}

	@Override
	public void changeItemInfo(Item item) throws DataAccessException {
		itemMapper.updateItem(item);
	}

	@Override
	public void deleteItem(String itemId) throws DataAccessException {
		itemMapper.deleteItem(itemId);
	}
	
	@Override
	public List<Goods> findAllGoods() throws DataAccessException {
		return itemMapper.selectAllGoods();
	}

	@Override
	public List<Goods> findGoodsByMember(String memberId) throws DataAccessException {
		return itemMapper.selectGoodsByMemberId(memberId);
	}

	@Override
	public List<Goods> findGoodsByMarket(int marketId) throws DataAccessException {
		return itemMapper.selectGoodsByMarketId(marketId);
	}

	@Override
	public List<Goods> findNewGoods() throws DataAccessException {
		return itemMapper.selectNewGoods();
	}

	@Override
	public Goods findGoodsByItem(String itemId) throws DataAccessException {
		return itemMapper.selectGoodsByItemId(itemId);
	}
	
	@Override
	public int findRemainQuantityByGoods(String itemId) throws DataAccessException {
		String remainQuantity = itemMapper.selectRemainQuantityByGoodsId(itemId);
		if (remainQuantity == null)
			return 0;
		return Integer.parseInt(remainQuantity);
	}

	@Override
	public void createGoods(Goods goods) throws DataAccessException {
		itemMapper.insertGoods(goods);
	}

	@Override
	public void changeGoodsInfo(Goods goods) throws DataAccessException {
		itemMapper.updateGoods(goods);
	}

	@Override
	public void changeRemainQuantityByOrderStatus(String itemId, int orderStatus, int quantity) 
			throws DataAccessException {
		itemMapper.updateRemainQuantity(itemId, orderStatus, quantity);
	}

	@Override
	public boolean isExistOrder(String itemId) throws DataAccessException {
		int ck = itemMapper.selectOrderCountByItemId(itemId);
		if (ck > 0) return true;
		return false;
	}

	@Override
	public List<ShareThing> findAllShareThing() throws DataAccessException {
		return itemMapper.selectAllShareThing();
	}

	@Override
	public List<ShareThing> findShareThingByMember(String memberId) throws DataAccessException {
		return  itemMapper.selectShareThingByMemberId(memberId);
	}

	@Override
	public List<ShareThing> findShareThingByMarket(int marketId) throws DataAccessException {
		return itemMapper.selectShareThingByMarketId(marketId);
	}

	@Override
	public ShareThing findShareThingByItem(String itemId) throws DataAccessException {
		return itemMapper.selectShareThingByItem(itemId);
	}

	@Override
	public void createShareThing(ShareThing shareThing) throws DataAccessException {
		itemMapper.insertShareThing(shareThing);
	}

	@Override
	public void changeShareThingInfo(ShareThing shareThing) throws DataAccessException {
		itemMapper.updateShareThing(shareThing);
	}

	@Override
	public boolean changeIsBorrowed(ShareThing shareThing) throws DataAccessException { // borrow service 수정 시 void로 변경해야 함
		int ck = itemMapper.updateIsBorrowed(shareThing);
		if (ck > 0) return true;
		return false;
	}

	@Override
	public boolean isExistBorrow(String itemId) throws DataAccessException {
		int ck = itemMapper.selectBorrowCountByItemId(itemId);
		if (ck > 0) return true;
		return false;
	}

}
