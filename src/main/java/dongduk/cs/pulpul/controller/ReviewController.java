package dongduk.cs.pulpul.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

import dongduk.cs.pulpul.domain.Review;
import dongduk.cs.pulpul.service.ReviewService;

@Controller
public class ReviewController implements ApplicationContextAware {

	private final ReviewService reviewSvc;
	private WebApplicationContext context;	
	private String uploadDir;
	
	@Autowired
	public ReviewController(ReviewService reviewSvc) {
		this.reviewSvc = reviewSvc;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.context = (WebApplicationContext) appContext;
		this.uploadDir = context.getServletContext().getRealPath("/upload/");
		// System.out.println(this.uploadDir);
	}
	
	/*
	 * 리뷰 작성 
	 */
	@PostMapping("/review")
	public String upload(Review review, FileCommand uploadFile, HttpSession session) {
		uploadFile.setPath(uploadDir);
		reviewSvc.addReview(review, uploadFile, (String)session.getAttribute("id"));
		
		return "redirect:/lookup/goodsDetail?itemId=" + review.getItem().getId();
	}
	
}
