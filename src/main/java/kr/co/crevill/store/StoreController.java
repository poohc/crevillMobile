package kr.co.crevill.store;

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
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("storeInfo.view")
	public ModelAndView list(HttpServletRequest request, StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("store/storeInfo");
		List<StoreVo> storeList = storeService.selectStoreList(storeDto);
		InstructorDto instructorDto = new InstructorDto();
		instructorDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		//TODO 매장코드 추가된다면 처리해줄것
		PlayDto playDto = new PlayDto();
		mav.addObject("list", storeList);
		mav.addObject("storeName", storeList.get(0).getStoreName());
		mav.addObject("nsList", staffService.selectInstructorList(instructorDto));
		mav.addObject("playList", playService.selectPlayList(playDto));
		return mav;
	}
}