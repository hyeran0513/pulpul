package dongduk.cs.pulpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import dongduk.cs.pulpul.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	@Modifying(clearAutomatically=true)
	@Query("UPDATE Member m " +
			"SET m.point=?1 " +
			"WHERE m.id=?2")
	void updatePoint(int point, String id);
}
