package dongduk.cs.pulpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dongduk.cs.pulpul.domain.Market;

public interface MarketRepository extends JpaRepository<Market, Integer> {
	
	Market findByMemberId(String memberId);
	
}