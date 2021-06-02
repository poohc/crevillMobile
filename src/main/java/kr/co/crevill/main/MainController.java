package kr.co.crevill.main;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.branches.BranchesService;
import kr.co.crevill.branches.NoticeDto;
import kr.co.crevill.common.CommonDto;
import kr.co.crevill.common.CommonService;
import kr.co.crevill.common.CommonVo;
import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.member.MemberDto;
import kr.co.crevill.member.MemberService;
import kr.co.crevill.play.PlayDto;
import kr.co.crevill.play.PlayService;
import kr.co.crevill.promotion.PromotionDto;
import kr.co.crevill.promotion.PromotionService;
import kr.co.crevill.reservation.ReservationService;
import kr.co.crevill.reservation.ReservationVo;
import kr.co.crevill.schedule.ScheduleDto;
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;
import kr.co.crevill.storeProgram.StoreProgramDto;
import kr.co.crevill.storeProgram.StoreProgramService;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherService;

@Controller
public class MainController {

	@Autowired
	private StoreService storeService; 
	
	@Autowired
	private StoreProgramService storeProgramService; 
	
	@Autowired
	private ReservationService reservationService; 
	
	@Autowired
	private MemberService memberService; 
	
	@Autowired
	private VoucherService voucherService;
	
	@Autowired
	private PromotionService promotionService; 
	
	@Autowired
	private PlayService playService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BranchesService branchesService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/")
	public ModelAndView mobile(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("main");
		return mav;
	}
	
	@GetMapping("main.view")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("main");
		StoreDto storeDto = new StoreDto();
		StoreProgramDto storeProgramDto = new StoreProgramDto();
		storeProgramDto.setLimit(3);
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		mav.addObject("programList", storeProgramService.selectStoreProgramList(storeProgramDto));
		
		List<ReservationVo> recommendListWeekday = reservationService.selectRecommendReservationWeekday();
		List<ReservationVo> recommendListWeekend = reservationService.selectRecommendReservationWeekend();
		
		List<ReservationVo> reccomendList = Stream.concat(recommendListWeekday.stream(), recommendListWeekend.stream()).collect(Collectors.toList());
		logger.info("중복 제거 전 예약 리스트 사이즈: " + reccomendList.size());
		reccomendList.stream().distinct().collect(Collectors.toList());
		logger.info("중복 제거 후 예약 리스트 사이즈: " + reccomendList.size());
		for(ReservationVo v : reccomendList) {
			logger.info("중복제거된 추천 예약 리스트 : " + v.getScheduleId());
		}
		//모바일회원이 아닐경우에만 추천 예약 리스트 노출
		if(!CrevillConstants.STORE_ID_MOBILE.equals(SessionUtil.getSessionMemberVo(request).getStoreId())) {
			mav.addObject("reccomendList", reccomendList);	
		}
		
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		memberDto.setQrCode(SessionUtil.getSessionMemberVo(request).getQrCode());
		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("voucherInfo", voucherService.getMemberVoucherInfo(voucherDto));
		mav.addObject("cellPhone", SessionUtil.getSessionMemberVo(request).getCellPhone());
		ScheduleDto scheduleDto = new ScheduleDto();
		scheduleDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("reservationList", reservationService.selectReservationList(scheduleDto));
		mav.addObject("voucherList", voucherService.getMemberVoucherAllList(voucherDto));
		storeDto.setLocation("SEOUL");
		mav.addObject("seoulStore", storeService.selectStoreList(storeDto));
		storeDto.setLocation("KYUNGKI");
		mav.addObject("kyungkiStore", storeService.selectStoreList(storeDto));
		storeDto.setLocation("NAMBU");
		mav.addObject("nambuStore", storeService.selectStoreList(storeDto));
		PromotionDto promotionDto = new PromotionDto();
		promotionDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		mav.addObject("promotionList", promotionService.getPromotionList(promotionDto));
		mav.addObject("visitSummary", memberService.selectVisitStoreSummary(memberDto));
		PlayDto playDto = new PlayDto();
		mav.addObject("playList", playService.selectPlayList(playDto));
		CommonDto commonDto = new CommonDto();
		commonDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		List<CommonVo> todayReservationList = commonService.selectTodayReservationInfo(commonDto);
		if(todayReservationList != null && todayReservationList.size() > 0) {
			mav.addObject("todayReservationList", todayReservationList);	
			mav.addObject("todayReservationListCnt", todayReservationList.size());
		}
		NoticeDto noticeDto = new NoticeDto(); 
		mav.addObject("noticeList", branchesService.selectNoticeList(noticeDto));
		return mav;
	}
	
	@GetMapping("main/guide.view")
	public ModelAndView guide(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/common/guide");
		return mav;
	}
}
