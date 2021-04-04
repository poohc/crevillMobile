package kr.co.crevill.reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.schedule.ScheduleDto;
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
@RequestMapping("reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private StoreService storeService;
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, ScheduleDto scheduleDto) {
		ModelAndView mav = new ModelAndView("reservation/list");
		scheduleDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("list", reservationService.selectReservationList(scheduleDto));
		return mav;
	}
	
	@GetMapping("search.view")
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("reservation/search");
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView regist(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("reservation/regist");
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result = reservationService.insertReservation(reservationDto, request);
		return result;
	}
	
	@PostMapping("getReservationList.proc")
	@ResponseBody
	public List<ReservationVo> getReservationList(HttpServletRequest request, @ModelAttribute ScheduleDto scheduleDto) {
		if(scheduleDto.getScheduleStart().isEmpty()) {
			scheduleDto.setScheduleType("ALL");
			scheduleDto.setScheduleStart(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		}
		return reservationService.selectReservationSearchList(scheduleDto);
	}
	
	@PostMapping("cancel.proc")
	@ResponseBody
	public JSONObject cancelProc(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result = reservationService.cancelReservation(reservationDto, request);
		return result;
	}
}