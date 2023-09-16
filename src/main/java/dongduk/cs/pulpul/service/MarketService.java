package dongduk.cs.pulpul.service;

import dongduk.cs.pulpul.controller.FileCommand;
import dongduk.cs.pulpul.domain.Market;

public interface MarketService {
	
	// 마켓 id로 마켓 조회
	Market getMarket(int marketId);
	
	// 회원 id로 마켓 조회
	Market getMarketByMember(String memberId);
	
	// 마켓 생성
	void makeMarket(Market market, FileCommand uploadFile);
	
	// 마켓 정보 수정
	void changeMarketInfo(Market market, FileCommand updateFile);
	
}
