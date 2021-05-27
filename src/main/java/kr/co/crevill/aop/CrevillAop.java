package kr.co.crevill.aop;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.common.MenuDto;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherService;

@Aspect
@Component
public class CrevillAop {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MenuDto menuDto;
	
	@Autowired
	private VoucherService voucherService;
	
	@Around("execution(* kr.co.crevill..*Controller.*(..))")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		String requestUri = request.getRequestURI();
		String contextPath = ""; 
		String servletPath = "";
		String servletMenuPath = "";
		
		logger.info("requestUri : " + requestUri);
		
		if(requestUri.split("/").length > 3) {
			contextPath = requestUri.split("/")[1];
			servletPath = requestUri.split("/")[2];	
			servletMenuPath = requestUri.split("/")[3];
		} 
		
		logger.info("servletPath : " + servletMenuPath);
		logger.info("servletMenuPath : " + servletMenuPath);
		
		//빈 INDEX 값일 경우 main.view 로 이동
		if("/mobile/".equals(requestUri)) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/main.view");
			return mav;
		}
		
		if(session.getAttribute("memberVo") == null && 
				!StringUtils.equalsAny(servletMenuPath, "main.view", "login.view", "passwordInit.view", "termsAgree.view", "join.view", "login.proc", "passwordInit.proc", "join.proc", "checkMemberCellPhone.proc", "join.proc", "getImage")) {
			
			if(!StringUtils.equalsAny(requestUri, "/mobile/main.view", "/mobile/play/list.view", "/mobile/play/detail.view", 
												  "/mobile/program/timeTable.view", "/mobile/program/timeTableDetail.view", 
												  "/mobile/store/storeList.view", "/mobile/store/storeInfo.view", "/mobile/main/guide.view")) {
				logger.info("여기로 오면 안되는데?");
				ModelAndView mav = new ModelAndView();
				mav.setViewName("redirect:/login/login.view");
				return mav;	
			}
			
		}
		
        try {
        	logger.info("==================== Logging 시작 ====================");
        	Map<String, String[]> requestMap = request.getParameterMap();
        	for(Entry<String, String[]> temp  : requestMap.entrySet()) {
        		logger.info("Request KEY : " + temp.getKey() + ", Value : " + ((String[])temp.getValue())[0]);
        	}
        	
        	logger.info("requestUri 확인 : " + requestUri);
        	logger.info("contextPath 확인 : " + contextPath + "/" + servletPath);
        	logger.info("servletPath 확인 : " + servletMenuPath);
        	
        	String menu = "";
        	
        	if(servletPath.indexOf("member") > -1) {
        		menuDto.setUpperMenu("고객관리");
        		if(servletMenuPath.indexOf("termsAgree.view") > -1) {
        			menu = "약관동의";
        		}
        		if(servletMenuPath.indexOf("join.view") > -1) {
        			menu = "회원가입";
        		}
        		if(servletMenuPath.indexOf("info.view") > -1) {
        			menu = "회원정보";
        		}
        		if(servletMenuPath.indexOf("update.view") > -1) {
        			menu = "고객정보수정";
        		}
        		if(servletMenuPath.indexOf("childList.view") > -1) {
        			menu = "자녀정보";
        		}
        		if(servletMenuPath.indexOf("childUpdate.view") > -1) {
        			menu = "자녀정보수정";
        		}
        		if(servletMenuPath.indexOf("childAdd.view") > -1) {
        			menu = "자녀등록";
        		}
        		if(servletMenuPath.indexOf("privacyAgree.view") > -1) {
        			menu = "개인정보취급방침";
        		}
        		if(servletMenuPath.indexOf("terms.view") > -1) {
        			menu = "이용약관";
        		}
        		if(servletMenuPath.indexOf("passwordChange.view") > -1) {
        			menu = "비밀번호 변경";
        		}
        	}
        	
        	if(servletPath.indexOf("staff") > -1) {
        		menuDto.setUpperMenu("직원관리");
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "직원리스트";
        		}
        		if(servletMenuPath.indexOf("regist.view") > -1) {
        			menu = "직원등록";
        		}
        		if(servletMenuPath.indexOf("nsList.view") > -1) {
        			menu = "원어민리스트";
        		}
        		if(servletMenuPath.indexOf("nsRegist.view") > -1) {
        			menu = "원어민등록";
        		}
        	}
        	
        	if(servletPath.indexOf("reservation") > -1) {
        		menuDto.setUpperMenu("예약업무");
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "예약리스트";
        		}
        		if(servletMenuPath.indexOf("todayList.view") > -1) {
        			menu = "당일예약조회";
        		}
        		if(servletMenuPath.indexOf("regist.view") > -1) {
        			menu = "예약등록";
        		}
        		if(servletMenuPath.indexOf("free.view") > -1) {
        			menu = "무료체험신청";
        		}
        	}
        	
        	if(servletPath.indexOf("branches") > -1) {
        		menuDto.setUpperMenu("지점업무");
        		if(servletMenuPath.indexOf("noticeWrite.view") > -1) {
        			menu = "공지작성";
        		}
        		if(servletMenuPath.indexOf("noticeList.view") > -1) {
        			menu = "공지리스트";
        		}
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menuDto.setUpperMenu("지점관리");
        			menu = "지점리스트";
        		}
        		if(servletMenuPath.indexOf("regist.view") > -1) {
        			menuDto.setUpperMenu("지점관리");
        			menu = "지점등록";
        		}
        	}
        	
        	if(servletPath.indexOf("play") > -1) {
        		menuDto.setUpperMenu("플레이관리");
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "프로그램 리스트";
        		}
        		if(servletMenuPath.indexOf("detail.view") > -1) {
        			menu = "프로그램 상세";
        		}
        	}
        	
        	if(servletPath.indexOf("schedule") > -1) {
        		menuDto.setUpperMenu("스케쥴관리");
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "스케쥴조회";
        		}
        		if(servletMenuPath.indexOf("regist.view") > -1) {
        			menu = "스케쥴등록";
        		}
        	}
        	
        	if(servletPath.indexOf("voucher") > -1) {
        		menuDto.setUpperMenu("바우처관리");
        		if(servletMenuPath.indexOf("regist.view") > -1) {
        			menu = "바우처등록";
        		}
        		if(servletMenuPath.indexOf("create.view") > -1) {
        			menu = "바우처생성";
        		}
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "바우처리스트";
        		}
        		if(servletMenuPath.indexOf("sale.view") > -1) {
        			menu = "바우처판매";
        		}
        	}
        	
        	if(servletPath.indexOf("program") > -1) {
        		menuDto.setUpperMenu("프로그램");
        		if(servletMenuPath.indexOf("timeTable.view") > -1) {
        			menu = "프로그램 시간표";
        		}
        		if(servletMenuPath.indexOf("timeTableDetail.view") > -1) {
        			menu = "매장 시간표";
        		}
        	}
        	
        	if(servletPath.indexOf("main") > -1) {
        		if(servletMenuPath.indexOf("guide.view") > -1) {
        			menu = "이용안내";
        		}
        	}
        	
        	if(servletPath.indexOf("promotion") > -1) {
        		if(servletMenuPath.indexOf("list.view") > -1) {
        			menu = "프로모션";
        		}
        	}
        	
        	if(servletPath.indexOf("store") > -1) {
        		menuDto.setUpperMenu("매장관리");
        		
        		if(servletMenuPath.indexOf("storeList.view") > -1) {
        			menu = "크레빌 센터";
        		}
        		
        		if(servletMenuPath.indexOf("storeInfo.view") > -1) {
        			menu = request.getParameter("storeName");
        		}
        	}
        	
        	menuDto.setMenuName(menu);
    		menuDto.setCurrentMenu(menu);
    		logger.info("menuDto : " + menuDto.toString());
        	request.setAttribute("menu", menuDto);
        	
        	VoucherDto voucherDto = new VoucherDto();
    		voucherDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
    		request.setAttribute("voucherInfo", voucherService.getMemberVoucherInfo(voucherDto));
    		contextPath = contextPath.replace("mobile/", "");
    		request.setAttribute("servletPath", servletPath);
        	logger.info("==================== Logging 종료 ====================");
        	Object result = joinPoint.proceed();
            return result;
        }catch (Exception e) {
        	logger.error("Logging 오류 : " + e);
        	return null;
        }
    }
}