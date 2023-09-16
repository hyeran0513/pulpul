package dongduk.cs.pulpul.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dongduk.cs.pulpul.domain.Review;

@Mapper
public interface ReviewMapper {
	
	List<Review> selectReviewByItem(String itemId);
	
	String selectReviewImage(int reviewId);
	
	String selectOrderIdByNotReview(@Param("itemId") String itemId, 
			@Param("memberId") String memberId);
	
	int selectReviewCountByItemId(@Param("itemId") String itemId,
			@Param("orderId") int orderId);
	
	void insertReview(Review review);
	
	void insertReviewImage(Review review);
	
}
