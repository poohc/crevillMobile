package kr.co.crevill.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.member.MemberDto;

@Controller
@RequestMapping("login")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("login.view")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/login/login");
		return mav;
	}
	
	@GetMapping("loginOut.view")
	public ModelAndView loginOut(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/login/login");
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
	
	@GetMapping("passwordInit.view")
	public ModelAndView passwordInit(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("/login/passwordInit");
		mav.addObject("cellPhone", request.getParameter("cellPhone"));
		return mav;
	}
	
	@PostMapping("passwordInit.proc")
	@ResponseBody
	public JSONObject passwordInitProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = loginService.passwordUpdate(memberDto, request);
		return result;
	}
}