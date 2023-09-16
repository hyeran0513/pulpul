package dongduk.cs.pulpul.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import dongduk.cs.pulpul.domain.Review;

public interface ReviewDao {
	
	// 품목 id로 리뷰 목록 조회
	List<Review> findReviewByItem(String itemId) throws DataAccessException;
	
	// 리뷰 이미지 조회
	String findReviewImage(int reviewId) throws DataAccessException;
	
	// 리뷰할 주문 id 1개 조회
	int findOrderIdByNotReview(String itemId, String memberId) throws DataAccessException;
	
	// 주문내역의 상품에 리뷰한 내역이 있는지 확인
	boolean isExistReview(String itemId, int orderId) throws DataAccessException;
	
	// 리뷰 생성
	void createReview(Review review) throws DataAccessException;
	
	// 리뷰 이미지 생성
	void createReviewImage(Review review) throws DataAccessException;
	
}
