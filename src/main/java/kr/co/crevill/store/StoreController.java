package kr.co.crevill.store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.play.PlayDto;
import kr.co.crevill.play.PlayService;
import kr.co.crevill.promotion.PromotionDto;
import kr.co.crevill.promotion.PromotionService;
import kr.co.crevill.staff.InstructorDto;
import kr.co.crevill.staff.StaffService;

@Controller
@RequestMapping("store")
public class StoreController {
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PlayService playService;
	
	@Autowired
	private PromotionService promotionService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("storeList.view")
	public ModelAndView storeList(HttpServletRequest request, StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("store/storeList");
		List<StoreVo> storeList = storeService.selectStoreList(storeDto);
		PromotionDto promotionDto = new PromotionDto();
		promotionDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		mav.addObject("promotionList", promotionService.getPromotionList(promotionDto));
		mav.addObject("list", storeList);
		return mav;
	}
	
	@GetMapping("storeInfo.view")
	public ModelAndView storeInfo(HttpServletRequest request, StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("store/storeInfo");
		List<StoreVo> storeList = storeService.selectStoreList(storeDto);
		PlayDto playDto = new PlayDto();
		mav.addObject("currentYear", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
		mav.addObject("currentMonth", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")));
		mav.addObject("currentDay", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")));
		mav.addObject("list", storeList);
		mav.addObject("storeName", storeList.get(0).getStoreName());
		mav.addObject("playList", playService.selectPlayList(playDto));
		StoreVo storeInfo = storeService.selectStoreInfo(storeDto);
		mav.addObject("info", storeInfo);
		InstructorDto instructorDto = new InstructorDto();
		instructorDto.setStoreId(storeDto.getStoreId());
		mav.addObject("nsList", staffService.selectInstructorList(instructorDto));
		mav.addObject("googleMapSrc", "https://www.google.com/maps?q="+storeInfo.getRoadAddress()+"&output=embed");
		return mav;
	}
}