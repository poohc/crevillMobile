package kr.co.crevill.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.crevill.member.MemberVo;

public class SessionUtil {
	public static MemberVo getSessionMemberVo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberVo memberVo = new MemberVo();
		if(session.getAttribute("memberVo") != null) {
			memberVo = (MemberVo) session.getAttribute("memberVo");	
		}
		return memberVo;
	}
}