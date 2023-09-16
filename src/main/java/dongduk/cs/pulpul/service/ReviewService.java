package dongduk.cs.pulpul.service;

import java.util.List;

import dongduk.cs.pulpul.controller.FileCommand;
import dongduk.cs.pulpul.domain.Review;

public interface ReviewService {
	
	// 품목 id로 리뷰 목록 조회
	List<Review> getReviewByItem(String itemId);
	
	// 리뷰해야 할 주문이 있다면 id 1개 반환
	int getOrderIdByNotReview(String itemId, String memberId);
	
	// 리뷰 작성
	void addReview(Review review, FileCommand uploadFiles, String memberId);
	
}
