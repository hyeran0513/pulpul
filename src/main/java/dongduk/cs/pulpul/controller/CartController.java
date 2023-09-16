package dongduk.cs.pulpul.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dongduk.cs.pulpul.domain.Cart;
import dongduk.cs.pulpul.domain.CartItem;
import dongduk.cs.pulpul.service.OrderService;
import dongduk.cs.pulpul.service.exception.AddCartException;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	private final OrderService orderSvc;
	
	@Autowired
	public CartController(OrderService orderSvc) {
		this.orderSvc = orderSvc;
	}
	
	/*
	 * 장바구니 목록 조회
	 */
	@GetMapping("/cartList")
	public String cartList(HttpSession session, Model model) {
		Cart cart = orderSvc.getCart((String) session.getAttribute("id"));
		if (cart != null) {
			model.addAttribute(cart);
			session.setAttribute("cart", cart);	// cart 객체를 세션에 저장
		}
		
		return "cart/cart";
	}
	
	/*
	 * 장바구니 상품 추가
	 */
	@PostMapping("/addItem")
	public String addCartItem(@Valid @ModelAttribute("cartItem") CartItem cartItem, BindingResult result,
			HttpSession session, RedirectAttributes rttr) {
		if (result.hasErrors()) {	// form 입력 값 검증
			rttr.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX +  "cartItem", result);
			rttr.addFlashAttribute("cartItem", cartItem);
			return "redirect:/lookup/goodsDetail?itemId=" + cartItem.getGoods().getItem().getId();
		}
		
		try {
			orderSvc.addCartItem((String) session.getAttribute("id"), cartItem);
			
			int newCartItemCnt = (int) session.getAttribute("cartItemCnt") + 1;	// 세션에 저장한 장바구니 상품 수 + 1
			session.setAttribute("cartItemCnt", newCartItemCnt);
			rttr.addFlashAttribute("addFailed", false);
			
		} catch(AddCartException e) {
			rttr.addFlashAttribute("addFailed", true);
			rttr.addFlashAttribute("exception", e.getMessage());
		}

		return "redirect:/lookup/goodsDetail?itemId=" + cartItem.getGoods().getItem().getId();
	}
	
	/*
	 * 장바구니에서 특정 상품 삭제
	 */
	@GetMapping("/deleteItem")
	public String deleteCartItem(@RequestParam("itemId") String itemId, HttpSession session) {
		orderSvc.deleteCartItem((String) session.getAttribute("id"), itemId);

		int newCartItemCnt = (int) session.getAttribute("cartItemCnt") - 1;	// 세션에 저장한 장바구니 상품 수 - 1
		session.setAttribute("cartItemCnt", newCartItemCnt);
		
		return "redirect:/cart/cartList";
	}

	/*
	 * 장바구니에서 특정 마켓의 상품 삭제
	 */
	@GetMapping("/deleteItemByMarket")
	public String deleteCartItemByMarket(@RequestParam("marketId") int marketId, HttpSession session) {
		int deleteCnt = orderSvc.deleteCartItemByMarket((String) session.getAttribute("id"), marketId);
		
		int newCartItemCnt = (int) session.getAttribute("cartItemCnt") - deleteCnt;	// 세션에 저장한 장바구니 상품 수 - 삭제한 장바구니 상품 수
		session.setAttribute("cartItemCnt", newCartItemCnt);
		
		return "redirect:/cart/cartList";
	}

}
