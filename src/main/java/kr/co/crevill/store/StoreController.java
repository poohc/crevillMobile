package kr.co.crevill.store;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.common.CommonCodeDto;
import kr.co.crevill.common.CommonService;

@Controller
@RequestMapping("store")
public class StoreController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StoreService storeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("store/list");
		mav.addObject("list", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView regist(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("store/regist");
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("STORE_TYPE");
		mav.addObject("storeType", commonService.selectCommonCode(commonCodeDto));
		commonCodeDto.setCodeType("CLASS_TYPE");
		mav.addObject("playList", commonService.selectCommonCode(commonCodeDto));
		return mav;
	}
	
	@GetMapping("update.view")
	public ModelAndView update(HttpServletRequest request, StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("store/update");
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("STORE_TYPE");
		mav.addObject("storeType", commonService.selectCommonCode(commonCodeDto));
		commonCodeDto.setCodeType("CLASS_TYPE");
		mav.addObject("playList", commonService.selectCommonCode(commonCodeDto));
		StoreVo storeVo = storeService.selectStoreInfo(storeDto);
		List<String> playKeyList = new ArrayList<String>();
		for(String playKey : storeVo.getPlayKey().split(",")) {
			playKeyList.add(playKey);
		}
		mav.addObject("info", storeVo);
		mav.addObject("playKeyList", playKeyList);
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute StoreDto storeDto) {
		JSONObject result = new JSONObject();
		result = storeService.insertStore(storeDto, request);
		return result;
	}
	
	@PostMapping("update.proc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request, @ModelAttribute StoreDto storeDto) {
		JSONObject result = new JSONObject();
		result = storeService.updateStore(storeDto, request);
		return result;
	}
	
	@PostMapping("delete.proc")
	@ResponseBody
	public JSONObject deleteProc(HttpServletRequest request, @ModelAttribute StoreDto storeDto) {
		JSONObject result = new JSONObject();
		result = storeService.deleteStore(storeDto, request);
		return result;
	}
}