package kr.co.crevill.program;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;
import kr.co.crevill.store.StoreVo;

@Controller
@RequestMapping("program")
public class ProgramController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ProgramService programService;
	
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
	public ModelAndView timetableDetail(HttpServletRequest request, @ModelAttribute StoreDto storeDto, @ModelAttribute ProgramDto programDto) {
		ModelAndView mav = new ModelAndView("program/timeTableDetail");
		programDto.setStoreId(storeDto.getStoreId());
		
		if(programDto.getPlayNameList() == null) {
			List<String> playNameList = new ArrayList<String>();
			playNameList.add("ART");
			playNameList.add("BOARD");
			playNameList.add("EIC");
			playNameList.add("GYM");
			playNameList.add("STORYTELLING");
			programDto.setPlayNameList(playNameList);
		} 
		if(programDto.getPlayNameListNext() == null) {
			List<String> playNameListNext = new ArrayList<String>();
			playNameListNext.add("ART");
			playNameListNext.add("BOARD");
			playNameListNext.add("EIC");
			playNameListNext.add("GYM");
			playNameListNext.add("STORYTELLING");
			programDto.setPlayNameListNext(playNameListNext);
			
		}
		mav.addObject("playNameList", programDto.getPlayNameList());
		mav.addObject("playNameListNext", programDto.getPlayNameListNext());
		programDto.setSearchMonth("thisMonth");
		mav.addObject("titleList", programService.selectTimeTitleList(programDto));
		mav.addObject("list", programService.selectTimeList(programDto));
		programDto.setSearchMonth("nextMonth");
		programDto.setPlayNameList(null);
		mav.addObject("titleListNext", programService.selectTimeTitleList(programDto));
		mav.addObject("listNext", programService.selectTimeList(programDto));
		mav.addObject("storeId", storeDto.getStoreId());
		StoreVo storeInfo = storeService.selectStoreInfo(storeDto);
		mav.addObject("storeInfo", storeInfo);
		mav.addObject("thisMonth", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")));
		mav.addObject("nextMonth", LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern("MM")));
		mav.addObject("tab", StringUtils.defaultIfEmpty(programDto.getTab(), "thisMonth"));
		return mav;
	}
	
	@PostMapping("getTimeTableList.proc")
	@ResponseBody
	public List<ProgramVo> getTimeTableList(HttpServletRequest request, @ModelAttribute ProgramDto programDto) {
		return programService.selectTimeList(programDto);
	}
}