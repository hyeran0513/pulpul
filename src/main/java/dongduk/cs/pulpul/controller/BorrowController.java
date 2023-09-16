package dongduk.cs.pulpul.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dongduk.cs.pulpul.domain.Borrow;
import dongduk.cs.pulpul.domain.Item;
import dongduk.cs.pulpul.domain.Member;
import dongduk.cs.pulpul.domain.ShareThing;
import dongduk.cs.pulpul.service.BorrowService;
import dongduk.cs.pulpul.service.ItemService;
import dongduk.cs.pulpul.service.MemberService;
import dongduk.cs.pulpul.validator.BorrowValidator;

@Controller
@RequestMapping("/borrow")
public class BorrowController {
	
	private final BorrowService borrowService;
	private final ItemService itemService;
	private final MemberService memberService;
	
	@Autowired 
	public BorrowController(BorrowService borrowService, ItemService itemService, MemberService memberService) {
		this.borrowService = borrowService;
		this.itemService = itemService;
		this.memberService = memberService;
	}
	
	@Autowired
	private BorrowValidator borrowValidator;
	public void setValidator(BorrowValidator borrowValidator) {
		this.borrowValidator = borrowValidator;
	}
	
	@ModelAttribute("borrow")
	public Borrow formBacking() {
		return new Borrow();
	}
	
	/*
	 * 공유물품 대여 신청 
	 */	
	@PostMapping("borrow")
	public String borrow(@ModelAttribute("borrow") Borrow borrow, Errors result, Model model, HttpServletRequest req){
		ShareThing shareThing = itemService.getShareThing(borrow.getShareThing().getItem().getId());
		borrowValidator.validate(borrow, result);
		if(result.hasErrors()) {
			borrow.setShareThing(shareThing);
			model.addAttribute("borrow", borrow);
			return "lookup/shareThingDetail";
		}
		
		HttpSession session = req.getSession();
		String borrowerId = (String) session.getAttribute("id");
		Member borrower = new Member();
		borrower.setId(borrowerId);
		borrow.setBorrower(borrower);
		
		boolean success = borrowService.borrow(borrow);
		
		if(success) {
			// lender에게 포인트 지급
			Member lender = memberService.getMember(borrow.getLender().getId());
			memberService.changePoint(lender, 1, 1000);
			
			return "redirect:/member/mypage";
		}
		else {
			borrow.setShareThing(shareThing);
			model.addAttribute("borrow", borrow);
			return "lookup/shareThingDetail";
		}
	}	

	/*
	 * 반납 처리
	 */
	@GetMapping("/return")
	public String returnItem(@RequestParam("borrowId") int borrowId, Model model) {
		Borrow borrow = borrowService.getBorrowById(borrowId);
		
		// 반납
		borrowService.returnShareThing(borrow);
		
		// borrower에게 포인트 지급
		Member borrower = memberService.getMember(borrow.getBorrower().getId());
		memberService.changePoint(borrower, 1, 1000);
		
		// 스케줄러 실행
		borrowService.reservationCancelScheduler(new Date());
		
		return "redirect:/market/shareThing/borrowList?itemId=" + borrow.getShareThing().getItem().getId();
	}

	/*
	 * 공유물품 예약
	 */
	@PostMapping("/reservation")
	public String makeReservation(@ModelAttribute("borrow") Borrow borrow, Model model, Errors result, HttpServletRequest req, RedirectAttributes rttr){
		HttpSession session = req.getSession();
		String id = (String) session.getAttribute("id");
		Member member = new Member();
		member.setId(id);
		borrow.setBorrower(member);
		
		// 중복 예약 확인
		int reservationCount = borrowService.getBorrowReservationCount(borrow);
		if (reservationCount != 0) { // 중복 예약일 시 reject
			model.addAttribute("reservationFailed", true);
		}
		else {
			// 예약
			boolean success = borrowService.makeBorrowReservation(borrow);
			if (success) {
				return "redirect:/member/mypage";
			}
		}
		ShareThing shareThing = itemService.getShareThing(borrow.getShareThing().getItem().getId());
		Borrow currBorrow = borrowService.getCurrBorrowByItem(borrow.getShareThing().getItem().getId());
		if (currBorrow == null) {
			currBorrow = new Borrow();
		}
		currBorrow.setShareThing(shareThing);
		model.addAttribute("borrow", currBorrow);
		return "lookup/shareThingDetail";
		
	}

	/*
	 * 공유물품 예약 취소
	 */
	@GetMapping("/reservation/cancel")
	public String cancel(@RequestParam("shareThingId") String shareThingId, @RequestParam("memberId") String memberId) {
		// shareThingId, borrowerId로 borrow 객체 생성
		Borrow borrow = new Borrow();
		ShareThing shareThing = new ShareThing();
		Item item = new Item();
		item.setId(shareThingId);
		shareThing.setItem(item);
		borrow.setShareThing(shareThing);
		Member borrower = new Member();
		borrower.setId(memberId);
		borrow.setBorrower(borrower);
		
		// 예약 취소
		borrowService.cancelBorrowReservation(borrow);
		return "redirect:/member/mypage";
	}

	/*
	 * 대여 연장
	 */
	@GetMapping("/extend")
	public String extend(@ModelAttribute("borrow") Borrow borrow, Errors result, HttpServletRequest req, Model model){
		Borrow borrowInfo = borrowService.getBorrowById(borrow.getId());
		
		if (borrowInfo.getIsExtended() == 1) {
			// 연장 기록이 있을 시 reject
			result.reject("alreadyExtended", new Object[] {borrowInfo.getShareThing().getItem().getName()}, null);
		}
		else {
			borrowService.extendBorrow(borrowInfo);
		}
		
		return "redirect:/member/mypage";
	}	
}
