package dongduk.cs.pulpul.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import dongduk.cs.pulpul.domain.Market;

@Mapper
public interface MarketMapper {
	
	Market selectMarketByMarketId(int marketId);
	
	Market selectMarketByMemberId(String memberId);
	
	String selectMarketImage(String memberId);
	
	void insertMarket(Market market);
	
	void insertMarketImage(Market market);
	
	void updateMarket(Market market);
	
	void deleteMarketImage(String memberId);
	
}
