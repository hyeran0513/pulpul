package dongduk.cs.pulpul.dao;

import org.springframework.dao.DataAccessException;

import dongduk.cs.pulpul.domain.Market;

public interface MarketDao {
	
	// 마켓 id로 마켓 정보 조회
	Market findMarket(int marketId) throws DataAccessException;
	
	// 회원 id로 마켓 정보 조회
	Market findMarketByMember(String memberId) throws DataAccessException;
	
	// 마켓 이미지 조회
	String findMarketImage(String memberId) throws DataAccessException;
	
	// 마켓 생성
	void createMarket(Market market) throws DataAccessException;
	
	// 마켓 이미지 생성
	void createMarketImage(Market market) throws DataAccessException;
	
	// 마켓 정보 수정
	void changeMarketInfo(Market market) throws DataAccessException;
	
	// 마켓 이미지 삭제
	void deleteMarketImage(String memberId) throws DataAccessException;

}
