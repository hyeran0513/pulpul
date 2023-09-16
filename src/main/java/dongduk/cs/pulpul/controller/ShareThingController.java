package dongduk.cs.pulpul.controller;

import java.util.ArrayList;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dongduk.cs.pulpul.domain.Borrow;
import dongduk.cs.pulpul.domain.ShareThing;
import dongduk.cs.pulpul.service.BorrowService;
import dongduk.cs.pulpul.service.ItemService;
import dongduk.cs.pulpul.service.exception.DeleteItemException;

@Controller
@RequestMapping("/market/shareThing")
@SessionAttributes("borrowList")
public class ShareThingController implements ApplicationContextAware {
	
	private final ItemService itemSvc;
	private final BorrowService borrowSvc;
	private WebApplicationContext context;	
	private String uploadDir;
	
	
	@Autowired
	public ShareThingController(ItemService itemSvc, BorrowService borrowSvc) {
		this.itemSvc = itemSvc;
		this.borrowSvc = borrowSvc;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.context = (WebApplicationContext) appContext;
		this.uploadDir = context.getServletContext().getRealPath("/upload/");
		// System.out.println(this.uploadDir);
	}
	
	@ModelAttribute("shareThing")
	public ShareThing formBacking() {
		return new ShareThing();
	}
	
	/*
	 * 마켓별 공유물품 목록 조회 - 마켓관리 > 공유물품 관리
	 */
	@GetMapping("/list")
	public String shareThingList(HttpSession session, Model model) {
		String memberId = (String) session.getAttribute("id");
		
		ArrayList<ShareThing> shareThingList = (ArrayList<ShareThing>) itemSvc.getShareThingListByMember(memberId);
		if(shareThingList != null) {
			model.addAttribute("shareThingList", shareThingList);
		}

		return "market/shareThingList";
	}

	/*
	 * 공유물품 등록 
	 */
	@GetMapping("/upload")
	public String uploadForm(@ModelAttribute("shareThing") ShareThing shareThing) {
		return "market/shareThingForm";
	}

	@PostMapping("/upload")
	public String upload(@Valid @ModelAttribute("shareThing") ShareThing shareThing, 
			BindingResult result, FileCommand uploadFiles) {
		if (result.hasErrors()) {
			return "market/shareThingForm";
		}

		uploadFiles.setPath(uploadDir);
		itemSvc.uploadShareThing(shareThing, uploadFiles);

		return "redirect:/market/shareThing/list";
	}
	
	/*
	 * 공유물품 수정
	 */
	@GetMapping("/update")
	public String updateForm(@ModelAttribute("shareThing") ShareThing shareThing,
			@RequestParam("itemId") String id) {
		ShareThing findShareThing = itemSvc.getShareThing(id);
		if (findShareThing != null) {
			BeanUtils.copyProperties(findShareThing, shareThing);
		}
		
		return "market/shareThingForm";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("shareThing") ShareThing shareThing, BindingResult result, 
			String[] deleteImages, FileCommand updateFiles) {
		if (result.hasErrors()) {
			return "market/shareThingForm";
		}
		
		updateFiles.setPath(uploadDir);
		itemSvc.changeShareThingInfo(shareThing, updateFiles, deleteImages);

		return "redirect:/market/shareThing/list";
	}
	
	/*
	 * 공유물품 삭제
	 */
	@GetMapping("/delete")
	public String delete(@RequestParam("itemId") String id, HttpSession session, 
			RedirectAttributes rttr) {
		try {
			itemSvc.deleteItem(id, (String)session.getAttribute("id"), uploadDir);
			
		} catch (DeleteItemException e) {
			rttr.addFlashAttribute("deleteFailed", true);
			rttr.addFlashAttribute("exception", e.getMessage());
		}
		
		return "redirect:/market/shareThing/list";
	}
	
	/*
     * 특정 공유물품의 대여 목록 조회
	 */
	@GetMapping("/borrowList")
	public String borrowList(@RequestParam("itemId") String itemId, Model model) {
		PagedListHolder<Borrow> borrowList = new PagedListHolder<Borrow>(borrowSvc.getBorrowByItem(itemId));
		if (borrowList != null) {
			borrowList.setPageSize(5);
			model.addAttribute("borrowList", borrowList);
		}
		
		return "market/shareThingBorrowList";
	}
	
	@GetMapping("/borrowList/{pageType}")
	public String borrowList2(@PathVariable("pageType") String page, 
			@ModelAttribute("borrowList") PagedListHolder<Borrow> borrowList, Model model) {
		if ("next".equals(page)) {
			borrowList.nextPage();
		}
		else if ("previous".equals(page)) {
			borrowList.previousPage();
		}

		model.addAttribute("borrowList", borrowList);
		
		return "market/shareThingBorrowList";
	}

}