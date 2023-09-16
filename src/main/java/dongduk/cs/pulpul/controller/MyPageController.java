package dongduk.cs.pulpul.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dongduk.cs.pulpul.domain.Borrow;
import dongduk.cs.pulpul.domain.Member;
import dongduk.cs.pulpul.domain.Order;
import dongduk.cs.pulpul.service.BorrowService;
import dongduk.cs.pulpul.service.MemberService;
import dongduk.cs.pulpul.service.OrderService;

@Controller
@RequestMapping("/member/mypage")
public class MyPageController {
	
	private final MemberService memberSvc;
	private final OrderService orderSvc;
	private final BorrowService borrowSvc;
	
	@Autowired 
	public MyPageController(MemberService memberSvc, OrderService orderSvc, BorrowService borrowSvc) {
		this.memberSvc = memberSvc;
		this.orderSvc = orderSvc;
		this.borrowSvc = borrowSvc;
	}
	
	@GetMapping()
	public String mypage(HttpSession session, Model model) {
		Member member = memberSvc.getMember((String) session.getAttribute("id"));
		if (member != null) {
			model.addAttribute(member);
		}
		
		return "member/mypage";
	}
	
	/*
	 * 구매목록 json으로 반환
	 */
	@GetMapping("/orderList")
	@ResponseBody
	public PagedListHolder<Order> myOrderList(HttpSession session) throws IOException {
		String memberId = (String) session.getAttribute("id");
		
		PagedListHolder<Order> orderList = new PagedListHolder<Order>(orderSvc.getOrderListByMember(memberId, "buyer"));
		if(orderList != null) {
			orderList.setPageSize(5);
			session.setAttribute("orderList", orderList);	// orderList 객체 세션에 저장
		}
		
		return orderList;
	}
	
	@GetMapping("/orderList/{page}")
	@ResponseBody
	public PagedListHolder<Order> myOrderList2(@PathVariable("page") String page,
			HttpSession session) throws IOException {
		@SuppressWarnings("unchecked")
		PagedListHolder<Order> orderList = (PagedListHolder<Order>) session.getAttribute("orderList");
		
		if ("next".equals(page)) {
			orderList.nextPage();
		}
		else if ("previous".equals(page)) {
			orderList.previousPage();
		}

		return orderList;
	}
	
	/*
	 * 구매 상세내역 조회
	 */
	@GetMapping("/orderDetail")
	public String orderDetail(@RequestParam("orderId") int orderId, Model model) {
		Order order = orderSvc.getOrder(orderId);
		model.addAttribute(order);

		return "member/orderDetail";
	}
	
	/*
	 * 대여, 예약목록 json으로 반환
	 */
	@GetMapping("/borrowList")
	@ResponseBody
	public PagedListHolder<Borrow> myBorrowList(HttpSession session) throws IOException {
		String memberId = (String) session.getAttribute("id");
		
		List<Borrow> borrows = borrowSvc.getBorrowByMember(memberId, "borrower");		
		List<Borrow> reservations = borrowSvc.getBorrowReservationByMember(memberId);
		
		PagedListHolder<Borrow> borrowList = null;
		if (borrows == null) {
			if(reservations != null) {	// 대여내역은 없고 예약내역만 존재하는 경우
				borrowList = new PagedListHolder<Borrow>(reservations);
			}
		}
		else {
			if(reservations != null) {	// 대여내역도 있고 예약내역도 존재하는 경우
				for (Borrow r : reservations) {
					borrows.add(r);
				}
			}
			borrowList = new PagedListHolder<Borrow>(borrows);
		}
		
		if(borrowList != null) {
			borrowList.setPageSize(5);
			session.setAttribute("borrowList", borrowList);
		}
		
		return borrowList;
	}
	
	@GetMapping("/borrowList/{page}")
	@ResponseBody
	public PagedListHolder<Borrow> myBorrowList2(@PathVariable("page") String page,
			HttpSession session) throws IOException {
		@SuppressWarnings("unchecked")
		PagedListHolder<Borrow> borrowList = (PagedListHolder<Borrow>) session.getAttribute("borrowList");
			
		if ("next".equals(page)) {
			borrowList.nextPage();
		}
		else if ("previous".equals(page)) {
			borrowList.previousPage();
		}
		
		return borrowList;
	}
}
