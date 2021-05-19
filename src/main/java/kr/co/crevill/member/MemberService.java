package kr.co.crevill.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.crevill.common.CommonMapper;
import kr.co.crevill.common.CommonUtil;
import kr.co.crevill.common.CrevillConstants;
import kr.co.crevill.common.FileDto;
import kr.co.crevill.common.FileVo;
import kr.co.crevill.common.SessionUtil;

@Service
public class MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private CommonMapper commonMapper;
	
	private final String NICE_SITE_CODE = "BU308";					// NICE로부터 부여받은 사이트 코드
	private final String NICE_SITE_PASSWORD = "0ZPyKHtAkoKO";		// NICE로부터 부여받은 사이트 패스워드
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	public MemberVo selectMemberInfo(MemberDto memberDto) {
		return memberMapper.selectMemberInfo(memberDto);
	}
	
	public List<MemberVo> selectChildMemberInfo(MemberDto memberDto){
		return memberMapper.selectChildMemberInfo(memberDto);
	}
	
	public List<MemberVo> selectChildMemberList(MemberDto memberDto){
		return memberMapper.selectChildMemberList(memberDto);
	}
	
	public List<MemberVo> selectVisitStoreList(MemberDto memberDto){
		return memberMapper.selectVisitStoreList(memberDto);
	}
	
	public String createNiceEncryptString(HttpServletRequest request) {
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
		HttpSession session = request.getSession();
		String sSiteCode = NICE_SITE_CODE;				// NICE로부터 부여받은 사이트 코드
	    String sSitePassword = NICE_SITE_PASSWORD;		// NICE로부터 부여받은 사이트 패스워드
	    String sRequestNumber = "";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
                                        	// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
	    sRequestNumber = niceCheck.getRequestNO(sSiteCode);
	    session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
	  	String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	   	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
		String customize 	= "Mobile";	//없으면 기본 웹페이지 / Mobile : 모바일페이지
		String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자 
		String sReturnUrl = request.getRequestURL().toString() + "?niceSucc=t";      // 성공시 이동될 URL
	    String sErrorUrl = request.getRequestURL().toString() + "?niceSucc=f";       // 실패시 이동될 URL

	    logger.info("sReturnUrl : " + sReturnUrl);
	    logger.info("sErrorUrl : " + sErrorUrl);
	    
	    // 입력될 plain 데이타를 만든다.
	    String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
	                        "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
	                        "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
	                        "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
	                        "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
	                        "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
	                        "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + 
							"6:GENDER" + sGender.getBytes().length + ":" + sGender;
	    
	    String sMessage = "";
	    String sEncData = "";
	    
	    int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
	    logger.info("NICE인증 iReturn : " + iReturn);
	    if( iReturn == 0 ){
	        sEncData = niceCheck.getCipherData();
	    } else if( iReturn == -1) {
	        sMessage = "암호화 시스템 에러입니다.";
	    } else if( iReturn == -2) {
	        sMessage = "암호화 처리오류입니다.";
	    } else if( iReturn == -3) {
	        sMessage = "암호화 데이터 오류입니다.";
	    } else if( iReturn == -9) {
	        sMessage = "입력 데이터 오류입니다.";
	    } else {
	        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	    }
	    logger.info("NICE인증 메시지 : " + sMessage);
	    logger.info("NICE인증 sEncData : " + sEncData);
	    return sEncData;
	}
	
	public String getNiceResult(HttpServletRequest request) {
		NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
		HttpSession session = request.getSession();
	    String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");
	    String sSiteCode = NICE_SITE_CODE;				// NICE로부터 부여받은 사이트 코드
	    String sSitePassword = NICE_SITE_PASSWORD;		// NICE로부터 부여받은 사이트 패스워드
	    String sCipherTime = "";			// 복호화한 시간
	    String sRequestNumber = "";			// 요청 번호
	    String sResponseNumber = "";		// 인증 고유번호
	    String sAuthType = "";				// 인증 수단
	    String sName = "";					// 성명
	    String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
	    String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
	    String sBirthDate = "";				// 생년월일(YYYYMMDD)
	    String sGender = "";				// 성별
	    String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
		String sMobileNo = "";				// 휴대폰번호
		String sMobileCo = "";				// 통신사
	    String sMessage = "";
	    String sPlainData = "";
	    
	    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
	    if( iReturn == 0 ){
	        sPlainData = niceCheck.getPlainData();
	        sCipherTime = niceCheck.getCipherDateTime();
	        
	        // 데이타를 추출합니다.
	        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
	        sRequestNumber  = (String)mapresult.get("REQ_SEQ");
	        sResponseNumber = (String)mapresult.get("RES_SEQ");
	        sAuthType		= (String)mapresult.get("AUTH_TYPE");
	        sName			= (String)mapresult.get("NAME");
			//sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
	        sBirthDate		= (String)mapresult.get("BIRTHDATE");
	        sGender			= (String)mapresult.get("GENDER");
	        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
	        sDupInfo		= (String)mapresult.get("DI");
	        sConnInfo		= (String)mapresult.get("CI");
	        sMobileNo		= (String)mapresult.get("MOBILE_NO");
	        sMobileCo		= (String)mapresult.get("MOBILE_CO");
	        
	        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
	        if(!sRequestNumber.equals(session_sRequestNumber)){
	            sMessage = "세션값 불일치 오류입니다.";
	            sResponseNumber = "";
	            sAuthType = "";
	        }
	    } else if( iReturn == -1) {
	        sMessage = "복호화 시스템 오류입니다.";
	    } else if( iReturn == -4) {
	        sMessage = "복호화 처리 오류입니다.";
	    } else if( iReturn == -5) {
	        sMessage = "복호화 해쉬 오류입니다.";
	    } else if( iReturn == -6) {
	        sMessage = "복호화 데이터 오류입니다.";
	    } else if( iReturn == -9) {
	        sMessage = "입력 데이터 오류입니다.";
	    } else if( iReturn == -12) {
	        sMessage = "사이트 패스워드 오류입니다.";
	    } else {
	        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	    }
	    logger.info("NICE 이름 : " + sName);
	    logger.info("NICE 전화번호 : " + sMobileNo);
	    logger.info("NICE 응답 sMessage : " + sMessage);
	    request.setAttribute("sName", sName);
	    request.setAttribute("sMobileNo", sMobileNo);
	    return sMessage;
	}
	
	public JSONObject checkMemberCellPhone(MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(memberMapper.checkExistCellPhone(memberDto) == 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject insertMemberInfo(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		memberDto.setRegId(CrevillConstants.REG_ID_MOBILE);
		memberDto.setStoreId(CrevillConstants.STORE_ID_MOBILE);
		//부모 정보 INSERT 성공 시 자녀정보 INSERT
		if(memberMapper.insertMemberParent(memberDto) > 0) {
			//자녀정보 INSERT 성공 시 resultCd = SUCC
			
			if(memberDto.getPicture() != null && !memberDto.getPicture().isEmpty()) {
				FileVo fileVo = commonMapper.selectImagesIdx();
				memberDto.setPictureIdx(fileVo.getImageIdx());
				FileDto fileDto = CommonUtil.setBlobByMultiPartFile(memberDto.getPicture());
				fileDto.setImageIdx(fileVo.getImageIdx());
				fileDto.setDescription("아이사진");
				commonMapper.insertImages(fileDto);
			}
			
			if(memberMapper.insertMemberChildren(memberDto) > 0) {
				//자녀 영어수준 INSERT 추가
				int learningGradeCount = memberDto.getLearningGrade().split(",").length;
				int tmpCnt = 0;
				
				if(learningGradeCount > 0) {
					for(String learningGrade : memberDto.getLearningGrade().split(",")) {
						memberDto.setLearningGrade(learningGrade);
						if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
							tmpCnt++;
						}
					}
					if(learningGradeCount == tmpCnt) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				} else {
					if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				}
			}
		}
		return result;
	}
	
	public JSONObject updateMemberInfo(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		memberDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		memberDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		//부모 정보 INSERT 성공 시 자녀정보 INSERT
		if(memberMapper.updateMemberParent(memberDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject updateChildMemberInfo(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		memberDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		memberDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(memberDto.getPicture() != null && !memberDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			memberDto.setPictureIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(memberDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("아이사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(memberMapper.updateMemberChild(memberDto) > 0) {
			
			if(memberMapper.deleteMemberChildrenGrade(memberDto) > 0) {
				//자녀 영어수준 INSERT 추가
				int learningGradeCount = memberDto.getLearningGrade().split(",").length;
				int tmpCnt = 0;
				
				if(learningGradeCount > 0) {
					for(String learningGrade : memberDto.getLearningGrade().split(",")) {
						memberDto.setLearningGrade(learningGrade);
						if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
							tmpCnt++;
						}
					}
					if(learningGradeCount == tmpCnt) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				} else {
					if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				}
			}
		}
		return result;
	}
	
	public JSONObject childAdd(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		memberDto.setRegId(CrevillConstants.REG_ID_MOBILE);
		memberDto.setStoreId(CrevillConstants.STORE_ID_MOBILE);
		if(memberDto.getPicture() != null && !memberDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			memberDto.setPictureIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(memberDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("아이사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(memberMapper.insertMemberChildren(memberDto) > 0) {
			//자녀 영어수준 INSERT 추가
			int learningGradeCount = memberDto.getLearningGrade().split(",").length;
			int tmpCnt = 0;
			
			if(learningGradeCount > 0) {
				for(String learningGrade : memberDto.getLearningGrade().split(",")) {
					memberDto.setLearningGrade(learningGrade);
					if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
						tmpCnt++;
					}
				}
				if(learningGradeCount == tmpCnt) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);
				}
			} else {
				if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);
				}
			}
		}
		return result;
	}
	
	private String requestReplace (String paramValue, String gubun) {

        String result = "";
        
        if (paramValue != null) {
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
        		paramValue = paramValue.replaceAll("=", "");
        	}
        	result = paramValue;
            
        }
        return result;
    }
	
	public JSONObject passwordChange(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		memberDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		memberDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(memberMapper.updateMemberParent(memberDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject deleteMemberInfo(MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(memberMapper.deleteMemberChildren(memberDto) > 0) {
			if(memberMapper.deleteMemberParent(memberDto) > 0) {
				if(memberMapper.deleteMemberChildrenGrade(memberDto) > 0) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);	
				}	
			}
		}
		return result;
	}
	
}