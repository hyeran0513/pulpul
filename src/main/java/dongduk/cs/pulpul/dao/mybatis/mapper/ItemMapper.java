package dongduk.cs.pulpul.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dongduk.cs.pulpul.domain.Goods;
import dongduk.cs.pulpul.domain.Item;
import dongduk.cs.pulpul.domain.ShareThing;

@Mapper
public interface ItemMapper {
	
	void insertItemImages(Map<String, Object> itemImages);
	
	int deleteItemImages(@Param("memberId") String memberId, 
			@Param("itemId") String itemId);
	
	void updateItem(Item item);
	
	void deleteItem(String itemId);
	
	List<Goods> selectAllGoods();
	
	List<Goods> selectGoodsByMemberId(String memberId);
	
	List<Goods> selectGoodsByMarketId(int marketId);
	
	List<Goods> selectNewGoods();
	
	Goods selectGoodsByItemId(String itemId);
	
	String selectRemainQuantityByGoodsId(String itemId);
	
	void insertGoods(Goods goods);
	
	void updateGoods(Goods goods);
	
	void updateRemainQuantity(@Param("itemId") String itemId, 
			@Param("orderStatus") int orderStatus, 
			@Param("quantity") int quantity);
	
	int selectOrderCountByItemId(String itemId);
	
	List<ShareThing> selectAllShareThing();
	
	List<ShareThing> selectShareThingByMemberId(String memberId);
	
	List<ShareThing> selectShareThingByMarketId(int marketId);
	
	ShareThing selectShareThingByItem(String itemId);
	
	void insertShareThing(ShareThing shareThing);
	
	void updateShareThing(ShareThing shareThing);
	
	int updateIsBorrowed(ShareThing shareThing);
	
	int selectBorrowCountByItemId(String itemId);
	
}
