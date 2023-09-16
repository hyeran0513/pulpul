package dongduk.cs.pulpul.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

import dongduk.cs.pulpul.domain.Market;
import dongduk.cs.pulpul.domain.Order;
import dongduk.cs.pulpul.service.MarketService;
import dongduk.cs.pulpul.service.OrderService;

@Controller
@RequestMapping("/market")
@SessionAttributes("orderList")
public class MarketController implements ApplicationContextAware {
	
	private final MarketService marketSvc;
	private final OrderService orderSvc;
	private WebApplicationContext context;	
	private String uploadDir;
	
	@Autowired
	public MarketController(MarketService marketSvc, OrderService orderSvc) {
		this.marketSvc = marketSvc;
		this.orderSvc = orderSvc;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.context = (WebApplicationContext) appContext;
		this.uploadDir = context.getServletContext().getRealPath("/upload/");
		// System.out.println(this.uploadDir);
	}
	
	@ModelAttribute("market")
	public Market formBacking(HttpSession session) {
		Market market = new Market();
		market.setMemberId((String) session.getAttribute("id"));
		market.setOpenStatus("0"); //openStatus를 0(비공개)로 초기화
		return market;
	}
	
	/*
	 * 마켓 정보 조회
	 */
	@GetMapping("/view")
	public String view(@ModelAttribute("market") Market market) {
		Market findMarket = marketSvc.getMarketByMember(market.getMemberId());
		if (findMarket != null) {
			BeanUtils.copyProperties(findMarket, market);
		}
		
		return "market/marketForm";
	}
	
	/*
	 * 마켓 등록
	 */ 
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("market") Market market, BindingResult result, 
			FileCommand uploadFile) {
		if (result.hasErrors()) {
			return "market/marketForm";
		}
		
		uploadFile.setPath(uploadDir);
		marketSvc.makeMarket(market, uploadFile); 

		return "redirect:/market/view";
	}
	
	/*
	 * 마켓 수정
	 */
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("market") Market market, BindingResult result,
			FileCommand updateFile) {
		if (result.hasErrors()) {
			return "market/marketForm";
		}
		
		updateFile.setPath(uploadDir);
		marketSvc.changeMarketInfo(market, updateFile); 

		return "redirect:/market/view";
	}

	/*
	 * 식물 판매목록 조회
	 */
	@GetMapping("/orderList")
	public String orderList(@RequestParam(required=false) String trackingNumber, 
			HttpSession session, Model model) {
		String memberId = (String) session.getAttribute("id");	
		String keyword = "seller";
		if (trackingNumber != null) {	// 운송장 번호로 검색 시 keyword 변수에 저장
			keyword = trackingNumber;
		}
		
		PagedListHolder<Order> orderList = new PagedListHolder<Order>(orderSvc.getOrderListByMember(memberId, keyword));
		if(orderList != null) {
			orderList.setPageSize(5);
			model.addAttribute("orderList", orderList);
		}
		
		return "market/orderList";
	}
	
	@GetMapping("/orderList/{pageType}")
	public String orderList2(@PathVariable("pageType") String page, 
			@ModelAttribute("orderList") PagedListHolder<Order> orderList, Model model) {	
		if ("next".equals(page)) {
			orderList.nextPage();
		}
		else if ("previous".equals(page)) {
			orderList.previousPage();
		}
		
		model.addAttribute("orderList", orderList);
		
		return "market/orderList";
	}
	
	/*
     * 식물 판매 상세내역 조회
	 */
	@GetMapping("/orderDetail")
	public String orderDetail(@RequestParam("orderId") int orderId, Model model) {
		Order order = orderSvc.getOrder(orderId);
		model.addAttribute("order", order);
		
		return "market/orderDetail";
	}
	
}
