package kr.co.crevill.reservation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import kr.co.crevill.play.PlayMapper;
import kr.co.crevill.schedule.ScheduleDto;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherMapper;

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
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String MSG_ALREADY_RESERVATION = "해당 회원으로 동일한 예약내역이 있습니다.";
	private final String MSG_CLASS_FULL = "해당 클래스는 예약이 모두 완료된 클래스입니다.";
	private final String MSG_TUTORING_FULL = "해당 튜터링은 예약이 모두 완료된 튜터링입니다.";
	private final String MSG_LESS_TIME_LEFT_VOUCHER = "바우처의 남은 시간이 부족합니다.";
	
	public int selectReservationCount(ScheduleDto scheduleDto) {
		return reservationMapper.selectReservationCount(scheduleDto);
	}
	
	public List<ReservationVo> selectReservationList(ScheduleDto scheduleDto){
		return reservationMapper.selectReservationList(scheduleDto);
	}
	
	public List<ReservationVo> selectReservationSearchList(ScheduleDto scheduleDto){
		return reservationMapper.selectReservationSearchList(scheduleDto);
	}
	
	public JSONObject insertReservation(ReservationDto reservationDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
//		reservationDto.setRegId(SessionUtil.getSessionStaffVo(request).getStaffId());
		reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_READY);
		
		/**
		 * 예약 유효성 체크
		 * 1. 회원으로 동일한 시간에 같은 클래스 예약이 있는지 체크
		 * 2. 해당 클래스 예약 인원 체크
		 * 3. 바우처 사용 시간 체크
		 */
		
		if(reservationMapper.checkAlreadyReservation(reservationDto) >= 1) {
			result.put("resultMsg", MSG_ALREADY_RESERVATION);
		} else {
			ReservationVo reservationVo = reservationMapper.checkReservationYn(reservationDto);
			if(reservationVo != null && "N".equals(reservationVo.getReservationYn())) {
				result.put("resultMsg", MSG_CLASS_FULL);
			} else {
				reservationVo = reservationMapper.checkTutoringReservationYn(reservationDto);
				if(reservationVo != null && "N".equals(reservationVo.getReservationYn())) {
					result.put("resultMsg", MSG_TUTORING_FULL);
				} else {
					reservationVo = reservationMapper.checkVoucherYn(reservationDto);
					if(reservationVo != null && "N".equals(reservationVo.getVoucherYn())) {
						result.put("resultMsg", MSG_LESS_TIME_LEFT_VOUCHER);
					} else {
						//선택 수업의 플레이타임 가져오기
						ReservationVo rVo = reservationMapper.selectReservationPlayInfo(reservationDto);
						EntranceDto entranceDto = new EntranceDto(); 
						entranceDto.setVoucherNo(reservationDto.getVoucherNo());
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
			}
		}
		
		return result;
	}
	
	public JSONObject cancelReservation(ReservationDto reservationDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_CANCEL);
//		reservationDto.setUpdId(SessionUtil.getSessionStaffVo(request).getStaffId());
		if(reservationMapper.updateReservation(reservationDto) > 0) {
			VoucherDto voucherDto = new VoucherDto();
			voucherDto.setVoucherUseId(reservationDto.getVoucherUseId());
			voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_CANCEL);;
//			voucherDto.setUpdId(SessionUtil.getSessionStaffVo(request).getStaffId());
			if(voucherMapper.updateVoucherUse(voucherDto) > 0) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);	
			}	
		}
		return result;
	}
}