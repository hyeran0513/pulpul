package dongduk.cs.pulpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import dongduk.cs.pulpul.domain.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
	
	Image findByMemberIdAndCategoryId(String memberId, String categoryId);
	
	@Modifying(clearAutomatically=true)
	void deleteByMemberIdAndCategoryId(String memberId, String categoryId);
	
}
