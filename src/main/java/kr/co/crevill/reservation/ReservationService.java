package kr.co.crevill.reservation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.crevill.common.CommonMapper;
import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.entrance.EntranceDto;
import kr.co.crevill.entrance.EntranceMapper;
import kr.co.crevill.member.MemberDto;
import kr.co.crevill.member.MemberMapper;
import kr.co.crevill.member.MemberVo;
import kr.co.crevill.play.PlayMapper;
import kr.co.crevill.schedule.ScheduleDto;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherMapper;
import kr.co.crevill.voucher.VoucherSaleDto;
import kr.co.crevill.voucher.VoucherVo;

@Service
public class ReservationService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private ReservationMapper reservationMapper;
	
	@Autowired
	private PlayMapper playMapper;
	
	@Autowired
	private EntranceMapper entranceMapper;
	
	@Autowired
	private VoucherMapper voucherMapper; 
	
	@Autowired
	private MemberMapper memberMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String MSG_ALREADY_RESERVATION = "해당 회원으로 동일한 예약내역이 있습니다.";
	private final String MSG_ALREADY_FREE_RESERVATION = "해당 회원으로 무료체험신청 내역이 있습니다.";
	private final String MSG_CLASS_FULL = "해당 클래스는 예약이 모두 완료된 클래스입니다.";
	private final String MSG_TUTORING_FULL = "해당 튜터링은 예약이 모두 완료된 튜터링입니다.";
	private final String MSG_LESS_TIME_LEFT_VOUCHER = "바우처의 남은 시간이 부족합니다.";
	private final String MSG_RESRVATION_PART_SUCC = "일부 예약이 실패했습니다. 상세 내역은 예약목록에서 확인해 주세요.";
	
	public int selectReservationCount(ScheduleDto scheduleDto) {
		return reservationMapper.selectReservationCount(scheduleDto);
	}
	
	public List<ReservationVo> selectReservationList(ScheduleDto scheduleDto){
		return reservationMapper.selectReservationList(scheduleDto);
	}
	
	public List<ReservationVo> selectReservationSearchList(ScheduleDto scheduleDto){
		return reservationMapper.selectReservationSearchList(scheduleDto);
	}
	
	public List<ReservationVo> selectRecommendReservationWeekday(){
		return reservationMapper.selectRecommendReservationWeekday();
	}
	
	public List<ReservationVo> selectRecommendReservationWeekend(){
		return reservationMapper.selectRecommendReservationWeekend();
	}
	
	public List<ReservationVo> selectQuickReservation(ScheduleDto scheduleDto){
		return reservationMapper.selectQuickReservation(scheduleDto);
	}

	public List<ReservationVo> selectAvaReservation(ReservationDto reservationDto){
		return reservationMapper.selectAvaReservation(reservationDto);
	}
	
	public List<ReservationVo> selectSearchDayReservation(ReservationDto reservationDto){
		return reservationMapper.selectSearchDayReservation(reservationDto);
	}
	
	public JSONObject insertReservation(ReservationDto reservationDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		reservationDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_READY);
		if(StringUtils.equalsAny(reservationDto.getClassType(), CrevillConstants.CLASS_TYPE_TUTORING)) {
			reservationDto.setTutoringYn("Y");
		} else {
			reservationDto.setTutoringYn("N");
		}
		int totalCnt = reservationDto.getChildName().split(",").length * reservationDto.getScheduleId().split(",").length; 
		int succCnt = 0;
		int fainCnt = 0;
		
		ReservationDto nReservationDto = new ReservationDto();
		nReservationDto.setVoucherNo(reservationDto.getVoucherNo());
		List<String> scheduleList = new ArrayList<String>();
		for(String scheduleId : reservationDto.getScheduleId().split(",")) {
			scheduleList.add(scheduleId);
		}
		nReservationDto.setScheduleIdList(scheduleList);
		
		//스케쥴에 잡힌 시간보다 바우처 남은 시간이 부족할 경우 전체 실패로 처리
		ReservationVo reservationVo = reservationMapper.checkVoucherYn(nReservationDto);
		if(reservationVo != null && "N".equals(reservationVo.getVoucherYn())) {
			result.put("resultMsg", MSG_LESS_TIME_LEFT_VOUCHER);
			return result;
		}
		
		//아이이름 X 스케쥴ID 개수만큼 FOR 문 돌면서 처리
		for(String childName : reservationDto.getChildName().split(",")) {
			for(String scheduleId : reservationDto.getScheduleId().split(",")) {
				nReservationDto = new ReservationDto();
				nReservationDto.setScheduleId(scheduleId);
				nReservationDto.setCellPhone(reservationDto.getCellPhone());
				nReservationDto.setClassType(reservationDto.getClassType());
				nReservationDto.setVoucherNo(reservationDto.getVoucherNo());
				nReservationDto.setChildName(childName);
				nReservationDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
				nReservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_READY);
				nReservationDto.setTutoringYn(reservationDto.getTutoringYn());
				/**
				 * 예약 유효성 체크
				 * 1. 회원으로 동일한 시간에 같은 클래스 예약이 있는지 체크
				 * 2. 해당 클래스 예약 인원 체크
				 * 3. 바우처 사용 시간 체크
				 */		
				if(reservationMapper.checkAlreadyReservation(nReservationDto) >= 1) {
					result.put("resultMsg", MSG_ALREADY_RESERVATION);
				} else {
					reservationVo = reservationMapper.checkReservationYn(nReservationDto);
					if(reservationVo != null && "N".equals(reservationVo.getReservationYn())) {
						result.put("resultMsg", MSG_CLASS_FULL);
					} else {
						reservationVo = reservationMapper.checkTutoringReservationYn(nReservationDto);
						if(reservationVo != null && "N".equals(reservationVo.getReservationYn())) {
							result.put("resultMsg", MSG_TUTORING_FULL);
						} else {
							//선택 수업의 플레이타임 가져오기
							ReservationVo rVo = reservationMapper.selectReservationPlayInfo(nReservationDto);
							EntranceDto entranceDto = new EntranceDto(); 
							entranceDto.setVoucherNo(nReservationDto.getVoucherNo());
			         	    entranceDto.setUseTime(rVo.getPlayTime());
			         	    entranceDto.setStatus(CrevillConstants.VOUCHER_STATUS_USED);
			         	    entranceDto.setScheduleId(nReservationDto.getScheduleId());
			         	    entranceDto.setRegId(reservationDto.getRegId());
							//바우처 사용 처리
			         	    if(entranceMapper.insertVoucherUse(entranceDto) > 0) {
			         	    	//예약등록 처리
			         	    	if(reservationMapper.insertReservation(nReservationDto) > 0) {
									succCnt++;
								}
							}
						}
					}
				}
			}
		}
		
		logger.info("예약총건수 : "+totalCnt+" + 예약성공 건수 : " + succCnt);
		
		if(totalCnt == succCnt) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		} else if(succCnt > 0 && totalCnt < succCnt) {
			result.put("resultMsg", MSG_RESRVATION_PART_SUCC);
		}
		
		return result;
	}
	
	public JSONObject insertFreeReservation(ReservationDto reservationDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		reservationDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_READY);
		reservationDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		reservationDto.setVoucherNo(CrevillConstants.EXPERIENCE_VOUCHER_NO);
		reservationDto.setTutoringYn("Y");
		/**
		 * 무료체험예약 유효성 체크
		 * 1. 무료체험예약이 있었는지 체크
		 * 2. 해당 튜터링 예약 인원 체크
		 */
		
		if(reservationMapper.checkAlreadyFreeReservation(reservationDto) >= 1) {
			result.put("resultMsg", MSG_ALREADY_FREE_RESERVATION);
		} else {
			ReservationVo reservationVo = reservationMapper.checkTutoringReservationYn(reservationDto);
			if(reservationVo != null && "N".equals(reservationVo.getReservationYn())) {
				result.put("resultMsg", MSG_TUTORING_FULL);
			} else {
				//선택 수업의 플레이타임 가져오기
				ReservationVo rVo = reservationMapper.selectReservationPlayInfo(reservationDto);
				EntranceDto entranceDto = new EntranceDto(); 
				entranceDto.setVoucherNo(CrevillConstants.EXPERIENCE_VOUCHER_NO);
         	    entranceDto.setUseTime(rVo.getPlayTime());
         	    entranceDto.setStatus(CrevillConstants.VOUCHER_STATUS_USED);
         	    entranceDto.setScheduleId(reservationDto.getScheduleId());
         	    entranceDto.setRegId(reservationDto.getRegId());
				//바우처 사용 처리
         	    if(entranceMapper.insertVoucherUse(entranceDto) > 0) {
         	    	//예약등록 처리
         	    	if(reservationMapper.insertReservation(reservationDto) > 0) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				}
			}
		}
		
		return result;
	}
	
	public JSONObject cancelReservation(ReservationDto reservationDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_CANCEL);
		reservationDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		if(reservationMapper.updateReservation(reservationDto) > 0) {
			VoucherDto voucherDto = new VoucherDto();
			voucherDto.setVoucherUseId(reservationDto.getVoucherUseId());
			voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_CANCEL);;
			voucherDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
			if(voucherMapper.updateVoucherUse(voucherDto) > 0) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);	
			}	
		}
		return result;
	}
	
	/**
	 * 무료체험이용여부체크 
	 * @methodName : checkFreeReservation
	 * @author : Juyoung Park
	 * @date : 2021.04.21
	 * @param request
	 * @return
	 */
	public JSONObject checkFreeReservation(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		List<VoucherVo> voucherList = voucherMapper.getMemberVoucherAllList(voucherDto);
		ReservationDto reservationDto = new ReservationDto(); 
		reservationDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		
		if(reservationMapper.checkAlreadyFreeReservation(reservationDto) > 0) {
			result.put("resultMsg", CrevillConstants.ALREADY_USE_FREE_RESERVATION_MSG);
		} else {
			if(voucherList != null && voucherList.size() > 0) {
				result.put("resultMsg", CrevillConstants.ALREADY_HAVE_VOUCHER_MSG);
			} else {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);		
			}
		}
		
		return result;
	}
	
	public JSONObject setNormalVoucher(HttpServletRequest request){
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setVoucherNo(voucherMapper.selectVoucherNo());
		voucherDto.setGrade(CrevillConstants.CREATE_NORMAL_VOUCHER_GRADE);
		voucherDto.setTicketName(CrevillConstants.CREATE_NORMAL_VOUCHER_TICKET_NAME);
		voucherDto.setPrice(CrevillConstants.CREATE_NORMAL_VOUCHER_PRICE);
		voucherDto.setSalePrice(CrevillConstants.CREATE_NORMAL_VOUCHER_PRICE);
		voucherDto.setUseTime(CrevillConstants.CREATE_NORMAL_VOUCHER_USE_TIME);
		voucherDto.setEndDate(CrevillConstants.CREATE_NORMAL_VOUCHER_END_DATE);
		voucherDto.setStoreId(CrevillConstants.CREATE_VOUCHER_STORE_ID);
		voucherDto.setAttribute(CrevillConstants.CREATE_NORMAL_VOUCHER_ATTRIBUTE);
		voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_READY);
		voucherDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		
		if(voucherMapper.insertVoucher(voucherDto) > 0) {
			//선택된 전달매체 모두 INSERT 성공해야 SUCC
			int cnt = voucherDto.getAttribute().split(",").length;
			int insCnt = 0;
			for(String attribute : voucherDto.getAttribute().split(",")) {
				voucherDto.setAttribute(attribute);
				if(voucherMapper.insertVoucherAttribute(voucherDto) > 0) {
					insCnt++;
				}	
			}
			//바우처 정상적으로 생성되면 바우처 판매처리
			if(cnt == insCnt) {
				VoucherSaleDto voucherSaleDto = new VoucherSaleDto();
				voucherSaleDto.setVoucherNo(voucherDto.getVoucherNo());
				voucherSaleDto.setBuyCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
				voucherSaleDto.setPgType(CrevillConstants.CREATE_NORMAL_VOUCHER_PG_TYPE);
				voucherSaleDto.setApprovalNo(CrevillConstants.CREATE_NORMAL_VOUCHER_APPROVAL_NO);
				voucherSaleDto.setStoreId(CrevillConstants.CREATE_VOUCHER_STORE_ID);
				voucherSaleDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
				MemberDto memberDto = new MemberDto();
				memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone()); 
				List<MemberVo> childList = memberMapper.selectChildMemberList(memberDto);
				if(childList != null && childList.size() > 0) {
					voucherSaleDto.setUsedChildrenName(childList.get(0).getChildName());
				}
				if(voucherMapper.insertVoucherSale(voucherSaleDto) > 0) {
					voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_SALE);
					if(voucherMapper.updateVoucher(voucherDto) > 0) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				}
			}
		}
		return result;
	}
}