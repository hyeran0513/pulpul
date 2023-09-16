package dongduk.cs.pulpul.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.domain.Member;
import dongduk.cs.pulpul.domain.Order;
import dongduk.cs.pulpul.service.MemberService;
import dongduk.cs.pulpul.service.OrderService;
import dongduk.cs.pulpul.service.exception.CancelOrderException;
import dongduk.cs.pulpul.service.exception.OrderException;

@Controller
@SessionAttributes("order")
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderSvc;
	private final MemberService memberSvc;
	
	@Autowired
	public OrderController(OrderService orderSvc, MemberService memberSvc) {
		this.orderSvc = orderSvc;
		this.memberSvc = memberSvc;
	}
	
	@ModelAttribute("order")
	public Order formBacking() {
		return new Order();
	}
	
	@ModelAttribute("cardCompanyList")
	public String[] createCardType() {
		return new String[]{"KB 국민", "비씨(페이북)", "신한카드", "NH농협", "1QPay(하나)", "씨티카드", "롯데카드", "UnionPay"};
	}
	
	/*
	 * 식물 구매 
	 */
	@GetMapping("/purchase")
	public String purchaseForm(@RequestParam("orderGoods") List<String> orderGoods,
			@ModelAttribute("order") Order order, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");	// 세션에서 Cart 객체 조회
		if (cart == null) {
			return "redirect:/cart/cartList";
		}
		
		List<CartItem> goodsList = new ArrayList<CartItem>();
		for (CartItem cartItem : cart.getCartItemList()) {
			for (String goodsId : orderGoods) {
				if (goodsId.equals(cartItem.getGoodsId())) {
					goodsList.add(cartItem);	// 장바구니에서 선택한 상품리스트 생성 후 반환
				}
			}
		}
		order.setGoodsList(goodsList);
		
		Member member = memberSvc.getMember((String) session.getAttribute("id"));
		order.setBuyer(member);
		
		return "order/purchase";
	}
	
	@PostMapping("/purchase")
	public String purchase(@Valid @ModelAttribute("order") Order order, BindingResult result,
			HttpSession session, SessionStatus status, Model model) {
		if (result.hasErrors()) {
			return "order/purchase";
		}
		
		int orderId;
		try {
			orderId = orderSvc.order(order);
			if (orderId > 0) {
				int newCartItemCnt = (int) session.getAttribute("cartItemCnt") - order.getGoodsList().size();	// 장바구니 상품 수 - 주문 상품 수
				session.setAttribute("cartItemCnt", newCartItemCnt);
			}
			
			if (!status.isComplete()) {
				status.setComplete(); // order 객체 참조 삭제
			}
			session.removeAttribute("cart"); // cart 객체를 세션에서 삭제
			
			return "redirect:/member/mypage/orderDetail?orderId=" + orderId;
			
		} catch (OrderException e) {
			model.addAttribute("orderFailed", true);
			model.addAttribute("exception", e.getMessage());
			return "order/purchase";
		}
		
	}
	
	/*
	 * 구매 취소
	 */
	@GetMapping("/cancel")
	public String cancel(@RequestParam("orderId") int orderId, RedirectAttributes rttr) {
		try {
			orderSvc.cancelOrder(orderId);

		} catch (CancelOrderException e) {
			rttr.addFlashAttribute("cancelFailed", true);
			rttr.addFlashAttribute("exception", e.getMessage());
		}
			
		return "redirect:/member/mypage";
	}
	
	/*
	 * 구매 확정
	 */
	@GetMapping("/finalize")
	public String finalize(@RequestParam("orderId") int orderId) {
		orderSvc.finalizeOrder(orderId);
			
		return "redirect:/member/mypage";
	}

	/*
	 * 특정 주문 내역 운송장 번호 입력
	 */
	@PostMapping("/startDeliver")
	public String startDeliver(Order order) {
		orderSvc.changeTrackingNumber(order);
		
		return "redirect:/market/orderList";
	}

}
