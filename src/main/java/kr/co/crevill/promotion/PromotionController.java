package kr.co.crevill.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
@RequestMapping("promotion")
public class PromotionController {
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private StoreService storeService;
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, @ModelAttribute PromotionDto promotionDto) {
		ModelAndView mav = new ModelAndView("promotion/list");
		mav.addObject("list", promotionService.selectPromotionList(promotionDto));
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView master(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("promotion/regist");
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute PromotionDto promotionDto) {
		JSONObject result = new JSONObject();
		result = promotionService.insertPromotion(promotionDto, request);
		return result;
	}
	
	@GetMapping("update.view")
	public ModelAndView master(HttpServletRequest request, PromotionDto promotionDto) {
		ModelAndView mav = new ModelAndView("promotion/update");
		PromotionVo promotionInfo = promotionService.selectPromotionInfo(promotionDto);
		List<String> checkedStoreList = new ArrayList<String>();
		for(String storeId : promotionInfo.getStoreId().split(",")) {
			checkedStoreList.add(storeId);
		}
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		mav.addObject("info", promotionInfo);
		mav.addObject("checkedStoreList", checkedStoreList);
		return mav;
	}
	
	@PostMapping("stop.proc")
	@ResponseBody
	public JSONObject stopProc(HttpServletRequest request, @ModelAttribute PromotionDto promotionDto) {
		JSONObject result = new JSONObject();
		result = promotionService.stopPromotion(promotionDto, request);
		return result;
	}
	
	@PostMapping("update.proc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request, @ModelAttribute PromotionDto promotionDto) {
		JSONObject result = new JSONObject();
		result = promotionService.updatePromotion(promotionDto, request);
		return result;
	}
	
	@PostMapping("delete.proc")
	@ResponseBody
	public JSONObject deleteProc(HttpServletRequest request, @ModelAttribute PromotionDto promotionDto) {
		JSONObject result = new JSONObject();
		result = promotionService.deletePromotion(promotionDto);
		return result;
	}
	
	@PostMapping("getPromotionList.proc")
	@ResponseBody
	public List<PromotionVo> getPromotionList(PromotionDto promotionDto){
		return promotionService.getPromotionList(promotionDto);
	}
}