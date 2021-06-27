package kr.co.crevill.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.member.MemberDto;
import kr.co.crevill.member.MemberMapper;
import kr.co.crevill.member.MemberVo;

@Service
public class LoginService {
	
	@Autowired
	private MemberMapper memberMapper; 
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public JSONObject loginProcess(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		result.put("resultMsg", CrevillConstants.RESULT_LOGIN_FAIL_MSG);
		
		//기존 회원 정보 없을 경우 
		if(memberMapper.checkExistCellPhone(memberDto) > 0) {
			
			MemberVo memberVo = memberMapper.selectMemberInfo(memberDto);
			
			if(memberVo == null) {
				result.put("resultCd",  CrevillConstants.RESULT_FAIL);
				result.put("resultMsg", CrevillConstants.RESULT_LOGIN_FAIL_MSG);	
			} else {
				if(memberVo.getPassword() != null && memberVo.getPassword().length() > 0) {
					
					//ID(휴대폰번호), 패스워드 일치할 경우
					if(memberDto.getPassword().equals(memberVo.getPassword())) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
						session.setAttribute("memberVo", memberVo);
					}
					
				} else {
					result.put("resultCd",  CrevillConstants.RESULT_PASSWORD_RESET);	//회원 정보는 있으나 패스워드 정보가 없을 경우 초기화 페이지 이동 처리
				}
			}
			
		} else {
			result.put("resultCd",  CrevillConstants.RESULT_FAIL);
			result.put("resultMsg", CrevillConstants.NOT_EXIST_CELL_PHONE);
		}
		 
		return result;
	}
	
	public JSONObject passwordUpdate(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		String updId = SessionUtil.getSessionMemberVo(request).getQrCode();
		if("".equals(String.valueOf(updId))) {	//비밀번호 최초 설정 시 세션 값이 없으므로 빈값일 경우는 UPD_ID 를 로그인 전화번호로 처리
			updId = memberDto.getCellPhone();	
		}
		memberDto.setUpdId(updId);
		if(memberMapper.updateMemberParent(memberDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
}