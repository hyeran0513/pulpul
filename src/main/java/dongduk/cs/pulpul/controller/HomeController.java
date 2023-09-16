package dongduk.cs.pulpul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import dongduk.cs.pulpul.domain.Goods;
import dongduk.cs.pulpul.service.ItemService;

@Controller
public class HomeController {

	private final ItemService itemService;
	
	@Autowired 
	public HomeController(ItemService itemService) {
		this.itemService = itemService;
	}
	
	@GetMapping("/") 
	public String index() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public ModelAndView home(ModelAndView mav) {
		List<Goods> newGoodsList = itemService.getNewGoodsList();	// 가장 최근 업로드된 3개의 상품 반환
		if (newGoodsList != null) {
			mav.addObject("newGoodsList", newGoodsList);
		}
		mav.setViewName("home");
		
		return mav;
	}
}
