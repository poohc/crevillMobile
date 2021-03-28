package kr.co.crevill;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
public class CrevillController {
	
	@Autowired
	private StoreService storeService; 
	
	@GetMapping("/")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("index");
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
}