package kr.co.crevill.login;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
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
import kr.co.crevill.member.MemberDto;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Controller
@RequestMapping("login")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("login.view")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/login/login");
		return mav;
	}
	
	@GetMapping("loginOut.view")
	public ModelAndView loginOut(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("");
		mav.setViewName("redirect:/main.view");
		HttpSession session = request.getSession();
		session.invalidate();
		return mav;
	}
	
	@PostMapping("login.proc")
	@ResponseBody
	public JSONObject loginProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = loginService.loginProcess(memberDto, request);
		return result;
	}
	
	@GetMapping("smsAuth.view")
	public ModelAndView smsAuth(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("/login/smsAuth");
		String cellPhone = String.valueOf(request.getParameter("cellPhone"));
		String devideCellPhone = "";
		if(!"null".equals(cellPhone)) {
			if(cellPhone.length() == 10) {
				devideCellPhone = cellPhone.substring(0,3) + "-" + cellPhone.substring(3,6) + "-" + cellPhone.substring(6,10);  
			} else if(cellPhone.length() == 11) {
				devideCellPhone = cellPhone.substring(0,3) + "-" + cellPhone.substring(3,7) + "-" + cellPhone.substring(7,11);
			}
		}
		
		mav.addObject("cellPhone", cellPhone);
		mav.addObject("devideCellPhone", devideCellPhone);
		return mav;
	}
	
	@GetMapping("passwordInit.view")
	public ModelAndView passwordInit(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("/login/passwordInit");
		
		String cellPhone = String.valueOf(request.getParameter("cellPhone"));
		String devideCellPhone = "";
		if(!"null".equals(cellPhone)) {
			if(cellPhone.length() == 10) {
				devideCellPhone = cellPhone.substring(0,3) + "-" + cellPhone.substring(3,6) + "-" + cellPhone.substring(6,10);  
			} else if(cellPhone.length() == 11) {
				devideCellPhone = cellPhone.substring(0,3) + "-" + cellPhone.substring(3,7) + "-" + cellPhone.substring(7,11);
			}
		}
		
		mav.addObject("cellPhone", cellPhone);
		mav.addObject("devideCellPhone", devideCellPhone);
		return mav;
	}
	
	@PostMapping("getAuthNum.proc")
	@ResponseBody
	public JSONObject getAuthNum(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		try {
			String apiKey = "NCSURH3AXXX7QLWF";
	        String apiSecret = "MVJ2O5SRBTQEIIO07LYCTQZEGCN2DZYU";
	        String authNum = RandomStringUtils.randomNumeric(6);
			if(authNum.length() == 6) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
				result.put("authNum", authNum);
				Message coolsms = new Message(apiKey, apiSecret);
			    HashMap<String, String> params = new HashMap<String, String>();
			    params.put("to", "01091091806");
			    params.put("from", "01030242890");
			    params.put("type", "SMS");
			    params.put("text", "[크레빌 인증번호] : " + authNum);
			    try {
					JSONObject json = (JSONObject) coolsms.send(params);
					logger.info("Coolsms 전송 결과 : " + json.toString());
					session.setAttribute("authNum", authNum);
				} catch (CoolsmsException e) {
					logger.error("Coolsms 오류 : " + e);
				}
			}
			
			
		} catch (Exception e) {
			logger.error("인증번호 가져오는 처리 중 오류 발생");
		}
		
		return result;
	}
	
	@PostMapping("checkAuthNum.proc")
	@ResponseBody
	public JSONObject checkAuthNum(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		HttpSession session = request.getSession();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		String sessionAuthNum = String.valueOf(session.getAttribute("authNum"));
		
		if(!"null".equals(sessionAuthNum)) {
			logger.info("세션저장된 인증번호 : " + sessionAuthNum + ", 넘어온 인증번호 : " + memberDto.getAuthNum());
			if(sessionAuthNum.equals(memberDto.getAuthNum())) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
			}
		}
		return result;
	}
	
	@PostMapping("passwordInit.proc")
	@ResponseBody
	public JSONObject passwordInitProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = loginService.passwordUpdate(memberDto, request);
		return result;
	}
}