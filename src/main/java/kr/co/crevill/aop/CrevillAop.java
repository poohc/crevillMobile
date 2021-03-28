package kr.co.crevill.aop;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

@Aspect
@Component
public class CrevillAop {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MenuDto menuDto;
	
	@Around("execution(* kr.co.crevill..*Controller.*(..))")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		String requestUri = request.getRequestURI();
		String contextPath = ""; 
		String servletPath = "";
		
		logger.info("requestUri : " + requestUri);
		
		if(requestUri.split("/").length > 1) {
			contextPath = requestUri.split("/")[1];
			servletPath = requestUri.split("/")[2];	
		} 
		
		logger.info("servletPath : " + servletPath);
		
		if(session.getAttribute("memberVo") == null && 
				!servletPath.contains("login") && 
					!servletPath.contains("passwordInit")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/login/login.view");
			return mav;
		}
		
        try {
        	logger.info("==================== Logging 시작 ====================");
        	Map<String, String[]> requestMap = request.getParameterMap();
        	for(Entry<String, String[]> temp  : requestMap.entrySet()) {
        		logger.info("Request KEY : " + temp.getKey() + ", Value : " + ((String[])temp.getValue())[0]);
        	}
        	
        	logger.info("requestUri 확인 : " + requestUri);
        	logger.info("contextPath 확인 : " + contextPath);
        	logger.info("servletPath 확인 : " + servletPath);
        	
        	String menu = "";
        	if(contextPath.indexOf("entrance") > -1) {
        		menuDto.setUpperMenu("고객입장");
        		if(servletPath.indexOf("member.view") > -1) {
        			menu = "회원입장";
        		}
        		if(servletPath.indexOf("nonMember.view") > -1) {
        			menu = "비회원입장";
        		}
        	}
        	
        	if(contextPath.indexOf("member") > -1) {
        		menuDto.setUpperMenu("고객관리");
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "고객리스트";
        		}
        		if(servletPath.indexOf("join.view") > -1) {
        			menu = "신규고객등록";
        		}
        	}
        	
        	if(contextPath.indexOf("staff") > -1) {
        		menuDto.setUpperMenu("직원관리");
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "직원리스트";
        		}
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "직원등록";
        		}
        		if(servletPath.indexOf("nsList.view") > -1) {
        			menu = "원어민리스트";
        		}
        		if(servletPath.indexOf("nsRegist.view") > -1) {
        			menu = "원어민등록";
        		}
        	}
        	
        	if(contextPath.indexOf("reservation") > -1) {
        		menuDto.setUpperMenu("예약업무");
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "예약리스트";
        		}
        		if(servletPath.indexOf("todayList.view") > -1) {
        			menu = "당일예약조회";
        		}
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "예약등록";
        		}
        	}
        	
        	if(contextPath.indexOf("branches") > -1) {
        		menuDto.setUpperMenu("지점업무");
        		if(servletPath.indexOf("noticeWrite.view") > -1) {
        			menu = "공지작성";
        		}
        		if(servletPath.indexOf("noticeList.view") > -1) {
        			menu = "공지리스트";
        		}
        		if(servletPath.indexOf("list.view") > -1) {
        			menuDto.setUpperMenu("지점관리");
        			menu = "지점리스트";
        		}
        		if(servletPath.indexOf("regist.view") > -1) {
        			menuDto.setUpperMenu("지점관리");
        			menu = "지점등록";
        		}
        	}
        	
        	if(contextPath.indexOf("play") > -1) {
        		menuDto.setUpperMenu("플레이관리");
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "플레이마스터";
        		}
        	}
        	
        	if(contextPath.indexOf("schedule") > -1) {
        		menuDto.setUpperMenu("스케쥴관리");
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "스케쥴조회";
        		}
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "스케쥴등록";
        		}
        	}
        	
        	if(contextPath.indexOf("voucher") > -1) {
        		menuDto.setUpperMenu("바우처관리");
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "바우처등록";
        		}
        		if(servletPath.indexOf("create.view") > -1) {
        			menu = "바우처생성";
        		}
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "바우처리스트";
        		}
        		if(servletPath.indexOf("sale.view") > -1) {
        			menu = "바우처판매";
        		}
        	}
        	
        	if(contextPath.indexOf("store") > -1) {
        		menuDto.setUpperMenu("매장관리");
        		if(servletPath.indexOf("list.view") > -1) {
        			menu = "매장리스트";
        		}
        		if(servletPath.indexOf("regist.view") > -1) {
        			menu = "매장등록";
        		}
        	}
        	
        	menuDto.setMenuName(menu);
    		menuDto.setCurrentMenu(menu);
    		logger.info("menuDto : " + menuDto.toString());
        	request.setAttribute("menu", menuDto);
        	logger.info("==================== Logging 종료 ====================");
        	Object result = joinPoint.proceed();
            return result;
        }catch (Exception e) {
        	logger.error("Logging 오류 : " + e);
        	return null;
        }
    }	
}