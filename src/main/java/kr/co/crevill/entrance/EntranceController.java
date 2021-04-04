package kr.co.crevill.entrance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("entrance")
public class EntranceController {
	
	@Autowired
	private EntranceService entranceService;
	
	@GetMapping("member.view")
	public ModelAndView member(HttpServletRequest request, EntranceDto entranceDto) {
		ModelAndView mav = new ModelAndView("entrance/member");
		mav.addObject("currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH")));
		entranceDto.setScheduleStart(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		mav.addObject("list", entranceService.selectEntranceList(entranceDto));
		return mav;
	}
	
	@GetMapping("nonMember.view")
	public ModelAndView nonMember(HttpServletRequest request, EntranceDto entranceDto) {
		ModelAndView mav = new ModelAndView("entrance/nonMember");
		mav.addObject("list", entranceService.selectNonMemberScheduleList());
		mav.addObject("voucherList", entranceService.selectNonMemberVoucherList(entranceDto));
		return mav;
	}
	
	@PostMapping("checkVoucher.proc")
	@ResponseBody
	public JSONObject checkVoucher(HttpServletRequest request, EntranceDto entranceDto) {
		JSONObject result = new JSONObject();
		result = entranceService.checkVoucher(entranceDto); 
		return result;
	}
	
	@PostMapping("entrance.proc")
	@ResponseBody
	public JSONObject entranceProc(HttpServletRequest request, EntranceDto entranceDto) {
		JSONObject result = new JSONObject();
		result = entranceService.entrance(entranceDto, request); 
		return result;
	}
	
	@PostMapping("nonMemberEntrance.proc")
	@ResponseBody
	public JSONObject nonMemberEntranceProc(HttpServletRequest request, EntranceDto entranceDto) {
		JSONObject result = new JSONObject();
		result = entranceService.nonMemberEntrance(entranceDto, request); 
		return result;
	}
}