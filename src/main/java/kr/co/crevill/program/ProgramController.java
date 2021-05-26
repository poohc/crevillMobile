package kr.co.crevill.program;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.common.CommonService;
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
@RequestMapping("program")
public class ProgramController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StoreService storeService;
	
	@GetMapping("timeTable.view")
	public ModelAndView timetable(HttpServletRequest request, @ModelAttribute StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("program/timeTable");
		mav.addObject("currentYear", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
		mav.addObject("currentMonth", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")));
		mav.addObject("currentDay", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")));
		mav.addObject("list", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@GetMapping("timeTableDetail.view")
	public ModelAndView timetableDetail(HttpServletRequest request, @ModelAttribute StoreDto storeDto) {
		ModelAndView mav = new ModelAndView("program/timeTableDetail");
		mav.addObject("currentYear", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
		mav.addObject("currentMonth", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")));
		mav.addObject("currentDay", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")));
		mav.addObject("info" , storeService.selectStoreInfo(storeDto));
		return mav;
	}
}