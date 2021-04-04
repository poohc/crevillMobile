package kr.co.crevill.entrance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.reservation.ReservationDto;
import kr.co.crevill.reservation.ReservationMapper;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherMapper;

@Service
public class EntranceService {

	@Autowired
	private EntranceMapper entranceMapper;
	
	@Autowired
	private ReservationMapper reservationMapper;
	
	@Autowired
	private VoucherMapper voucherMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<EntranceVo> selectEntranceList(EntranceDto entranceDto){
		return entranceMapper.selectEntranceList(entranceDto);
	}
	
	public EntranceVo selectNonMemberScheduleList(){
		return entranceMapper.selectNonMemberScheduleList();
	}
	
	public List<EntranceVo> selectNonMemberVoucherList(EntranceDto entranceDto){
		return entranceMapper.selectNonMemberVoucherList(entranceDto);
	}
	
	public JSONObject checkVoucher(EntranceDto entranceDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		EntranceVo entranceVo = entranceMapper.checkVoucher(entranceDto); 
		if("Y".equals(entranceVo.getVoucherAvailableYn())) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject entrance(EntranceDto entranceDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		EntranceVo entranceVo = entranceMapper.selectEntranceInfo(entranceDto);
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		entranceDto.setVoucherNo(entranceVo.getVoucherNo());
		entranceDto.setUseTime(entranceVo.getPlayTime());
		entranceDto.setStatus(CrevillConstants.VOUCHER_STATUS_USED);
		entranceDto.setScheduleId(entranceVo.getScheduleId());
		entranceDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
 	    entranceDto.setChildName(entranceVo.getChildName());
 	    entranceDto.setChildBirthday(entranceVo.getChildBirthday());
 	    entranceDto.setMemberQrCode(entranceVo.getMemberQrCode());
 	    entranceDto.setChildSex(entranceVo.getChildSex());
 	    
		if(entranceMapper.insertScheduleEntranceMember(entranceDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	/**
	 * 비회원 입장처리
	 * @methodName : nonMemberEntrance
	 * @author : Juyoung Park
	 * @date : 2021.04.01
	 * @param entranceDto
	 * @param request
	 * @return
	 */
	public JSONObject nonMemberEntrance(EntranceDto entranceDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);

		entranceDto.setUseTime(entranceDto.getPlayTime());
		entranceDto.setStatus(CrevillConstants.VOUCHER_STATUS_USED);
		entranceDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		
		//바우처 상태 업데이트
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setVoucherNo(entranceDto.getVoucherNo());
		voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_SALE);
		if(voucherMapper.updateVoucher(voucherDto) > 0) {
			if(entranceMapper.insertVoucherUse(entranceDto) > 0) {
				 ReservationDto reservationDto = new ReservationDto();
				 reservationDto.setCellPhone(entranceDto.getCellPhone());
				 reservationDto.setVoucherNo(entranceDto.getVoucherNo());
				 reservationDto.setScheduleId(entranceDto.getScheduleId());
				 reservationDto.setStatus(CrevillConstants.RESERVATION_STATUS_COMPLETE);
				 reservationDto.setRegId(entranceDto.getRegId());
				 //예약등록 처리
	 	    	if(reservationMapper.insertReservation(reservationDto) > 0) {
					//입장처리
	 	    		if(entranceMapper.insertScheduleEntranceMember(entranceDto) > 0) {
		 	   			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		 	   		}
				}	
			}
		}
		return result;
	}
	
}