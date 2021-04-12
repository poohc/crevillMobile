package kr.co.crevill.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import kr.co.crevill.common.CommonService;
import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
@RequestMapping("schedule")
public class ScheduleController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private StoreService storeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, ScheduleDto scheduleDto) {
		ModelAndView mav = new ModelAndView("schedule/list");
		scheduleDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		scheduleDto.setScheduleType("ALL");
		mav.addObject("allList", scheduleService.selectScheduleList(scheduleDto));
		scheduleDto.setScheduleType("ING");
		mav.addObject("ingList", scheduleService.selectScheduleList(scheduleDto));
		scheduleDto.setScheduleType("END");
		mav.addObject("endList", scheduleService.selectScheduleList(scheduleDto));
		
		String scheduleDate = "";
		if(scheduleDto.getScheduleStart() != null) {
			scheduleDate = scheduleDto.getScheduleStart().substring(0,4) + "-" + scheduleDto.getScheduleStart().substring(4,6) + "-" + scheduleDto.getScheduleStart().substring(6,8);
			mav.addObject("scheduleDate", scheduleDate);
		}
		
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView regist(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("schedule/regist");
		List<String> timeList = new ArrayList<String>();
		for(int i=0; i<24; i++) {
			if(i >= 10) {
				timeList.add(i + "");
			} else {
				timeList.add("0" + i);
			}
		}
		mav.addObject("timeList", timeList);
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute ScheduleDto scheduleDto) {
		JSONObject result = new JSONObject();
		result = scheduleService.insertSchedule(scheduleDto, request);
		return result;
	}
	
	@PostMapping("getScheduleList.proc")
	@ResponseBody
	public JSONObject getScheduleList(HttpServletRequest request, ScheduleDto scheduleDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(scheduleDto.getScheduleStart() == null || scheduleDto.getScheduleStart().isEmpty() || scheduleDto.getScheduleStart() == "") {
			scheduleDto.setScheduleStart(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		}
		
		if(scheduleDto.getScheduleType() == null || scheduleDto.getScheduleType().isEmpty()) {
			scheduleDto.setScheduleType("ALL");	
		}
		
		List<ScheduleVo> scheduleList = scheduleService.selectScheduleList(scheduleDto);
		if(scheduleList != null && scheduleList.size() > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
			result.put("scheduleList", scheduleList);
		}
		return result;
	}
}