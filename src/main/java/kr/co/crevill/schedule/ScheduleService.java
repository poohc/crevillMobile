package kr.co.crevill.schedule;

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
import kr.co.crevill.play.PlayDto;
import kr.co.crevill.play.PlayMapper;
import kr.co.crevill.play.PlayVo;

@Service
public class ScheduleService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private ScheduleMapper scheduleMapper;
	
	@Autowired
	private PlayMapper playMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectScheduleCount(ScheduleDto scheduleDto) {
		return scheduleMapper.selectScheduleCount(scheduleDto);
	}
	
	public List<ScheduleVo> selectScheduleList(ScheduleDto scheduleDto){
		return scheduleMapper.selectScheduleList(scheduleDto);
	}
	
	public JSONObject insertSchedule(ScheduleDto scheduleDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(scheduleDto.getOperationType().split(",").length > 1) {
			scheduleDto.setOperationType("BOTH");
		}
		
		PlayDto playDto = new PlayDto();
		playDto.setPlayId(scheduleDto.getPlayId());
		PlayVo playVo = playMapper.selectPlayInfo(playDto);
		scheduleDto.setScheduleEnd(playVo.getPlayTime());
		scheduleDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		
		//직원정보 INSERT 
		if(scheduleMapper.insertSchedule(scheduleDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	
	
}