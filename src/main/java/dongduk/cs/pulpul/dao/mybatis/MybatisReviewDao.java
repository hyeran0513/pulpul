package dongduk.cs.pulpul.dao.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import dongduk.cs.pulpul.dao.ReviewDao;
import dongduk.cs.pulpul.dao.mybatis.mapper.ReviewMapper;
import dongduk.cs.pulpul.domain.Review;

@Repository
public class MybatisReviewDao implements ReviewDao {

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public List<Review> findReviewByItem(String itemId) throws DataAccessException {
		return reviewMapper.selectReviewByItem(itemId);
	}
	
	@Override
	public String findReviewImage(int reviewId) throws DataAccessException {
		return reviewMapper.selectReviewImage(reviewId);
	}

	@Override
	public int findOrderIdByNotReview(String itemId, String memberId) throws DataAccessException {
		String orderId = reviewMapper.selectOrderIdByNotReview(itemId, memberId);
		if (orderId == null)
			return 0;
		return Integer.parseInt(orderId);
	}
	
	@Override
	public boolean isExistReview(String itemId, int orderId) throws DataAccessException {
		int ck = reviewMapper.selectReviewCountByItemId(itemId, orderId);
		if (ck > 0) return true;
		return false;
	}

	@Override
	public void createReview(Review review) throws DataAccessException {
		reviewMapper.insertReview(review);
	}

	@Override
	public void createReviewImage(Review review) throws DataAccessException {
		reviewMapper.insertReviewImage(review);
	}

}
