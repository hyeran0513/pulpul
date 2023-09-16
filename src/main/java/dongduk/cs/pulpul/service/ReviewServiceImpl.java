package dongduk.cs.pulpul.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dongduk.cs.pulpul.controller.FileCommand;
import dongduk.cs.pulpul.dao.MemberDao;
import dongduk.cs.pulpul.dao.ReviewDao;
import dongduk.cs.pulpul.domain.Member;
import dongduk.cs.pulpul.domain.Review;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private MemberDao memberDao;

	@Override
	public List<Review> getReviewByItem(String itemId) {
		List<Review> reviewList = reviewDao.findReviewByItem(itemId);
		for (Review review : reviewList) {
			review.setImageUrl(reviewDao.findReviewImage(review.getId()));
		}
		return reviewList;
	}

	@Override
	public int getOrderIdByNotReview(String itemId, String memberId) {
		return reviewDao.findOrderIdByNotReview(itemId, memberId);
	}

	@Transactional
	@Override
	public void addReview(Review review, FileCommand uploadFile, String memberId) {
		reviewDao.createReview(review);	// 리뷰 레코드 생성
		
		if (!uploadFile.getFile().isEmpty()) {
			String filename = uploadFile(uploadFile, review.getId());	// 리뷰 이미지 파일 저장
			review.setImageUrl("/upload/" + filename);
			review.getOrder().setBuyer(new Member());
			review.getOrder().getBuyer().setId(memberId);
			reviewDao.createReviewImage(review);	// 리뷰 이미지 레코드 생성
		}
		memberDao.changePoint(memberId, 1, 50);	// 포인트 +50
	}
	
	// 이미지 파일 업로드 메소드
	public String uploadFile(FileCommand uploadFile, int reviewId) {
		String newFilename = "";
		try {       
            newFilename = "RIMG-" + reviewId + ".jpg";
            
            File newFile = new File(uploadFile.getPath() + newFilename);
            if (newFile.exists())
            	newFile.delete();
            uploadFile.getFile().transferTo(newFile);

        }catch(Exception e) {            
            e.printStackTrace();
        }
		return newFilename;
	}

}
