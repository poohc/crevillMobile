package kr.co.crevill.reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.member.MemberDto;
import kr.co.crevill.member.MemberService;
import kr.co.crevill.schedule.ScheduleDto;
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherSaleDto;
import kr.co.crevill.voucher.VoucherService;
import kr.co.crevill.voucher.VoucherVo;

@Controller
@RequestMapping("reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private VoucherService voucherService;
	
	@Autowired
	private MemberService memberService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		storeDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		VoucherSaleDto voucherSaleDto = new VoucherSaleDto();
		voucherSaleDto.setBuyCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("cellPhone", SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		voucherSaleDto.setIsBasic("false");
		mav.addObject("voucherList", voucherService.getMemberVoucherList(voucherSaleDto));
		return mav;
	}
	
	@GetMapping("shortRegist.view")
	public ModelAndView shortRegist(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("reservation/shortRegist");
		StoreDto storeDto = new StoreDto();
		storeDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		mav.addObject("storeList", storeService.selectShortVoucherStoreList(storeDto));
		VoucherSaleDto voucherSaleDto = new VoucherSaleDto();
		voucherSaleDto.setBuyCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		voucherSaleDto.setIsBasic("true");
		List<VoucherVo> voucherList = voucherService.getMemberVoucherList(voucherSaleDto);
		
		int basicVoucherCount = 0; 
		//보유 바우처 중 1회권이 하나도 없으면 생성		
		
		if(voucherList != null) {
			for(VoucherVo tempVoucher : voucherList) {
				if("1회권".equals(tempVoucher.getTicketName())) {
					basicVoucherCount++;
					break;
				}
			}
		}
		
		logger.info("basicVoucherCount : " + basicVoucherCount);
		
		//1회권이 하나도 없을 경우 생성
		if(basicVoucherCount == 0) {
			reservationService.setNormalVoucher(request);	
		}
		
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("cellPhone", SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		voucherSaleDto.setIsBasic("true");
		List<VoucherVo> memVoucherList = voucherService.getMemberVoucherList(voucherSaleDto);
		mav.addObject("voucherList", memVoucherList);
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setVoucherNo(memVoucherList.get(0).getVoucherNo());
		mav.addObject("voucherAttributeList", voucherService.selectVoucherAttributeList(voucherDto));
		
		return mav;
	}
	
	@GetMapping("free.view")
	public ModelAndView free(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("reservation/freeRegist");
		StoreDto storeDto = new StoreDto();
		storeDto.setExperienceClass("Y");
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		return mav;
	}
	
	@PostMapping("freeCheck.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result = reservationService.checkFreeReservation(request);
		return result;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result = reservationService.insertReservation(reservationDto, request);
		return result;
	}
	
	@PostMapping("freeRegist.proc")
	@ResponseBody
	public JSONObject freeRegistProc(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result = reservationService.insertFreeReservation(reservationDto, request);
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
	
	@PostMapping("getQuickReservationInfo.proc")
	@ResponseBody
	public JSONObject getQuickReservationInfo(HttpServletRequest request, @ModelAttribute ScheduleDto scheduleDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		scheduleDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		List<ReservationVo> reservationList = reservationService.selectQuickReservation(scheduleDto);
		if(reservationList != null && reservationList.size() > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
			result.put("scheduleList", reservationList);
		}
		return result;
	}
	
	@PostMapping("getAvaReservationList.proc")
	@ResponseBody
	public JSONObject getAvaReservationList(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
//		if(!CrevillConstants.STORE_ID_MOBILE.equals(SessionUtil.getSessionMemberVo(request).getStoreId())) {
//			reservationDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());	
//		}
		
		reservationDto.setChildLength(reservationDto.getChildName().split(",").length - 1);
		
		List<ReservationVo> avaReservationList = reservationService.selectAvaReservation(reservationDto);
		if(avaReservationList != null && avaReservationList.size() > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
			result.put("list", avaReservationList);
		}
		return result;
	}
	
	@PostMapping("getSearchDayReservation.proc")
	@ResponseBody
	public JSONObject getSearchDayReservation(HttpServletRequest request, @ModelAttribute ReservationDto reservationDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
//		if(!CrevillConstants.STORE_ID_MOBILE.equals(SessionUtil.getSessionMemberVo(request).getStoreId())) {
//			reservationDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());	
//		}
		reservationDto.setChildLength(reservationDto.getChildName().split(",").length - 1);
		
		List<ReservationVo> list = reservationService.selectSearchDayReservation(reservationDto);
		if(list != null && list.size() > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
			result.put("list", list);
		}
		return result;
	}
	
}