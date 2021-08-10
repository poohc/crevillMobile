package kr.co.crevill.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.common.CommonCodeDto;
import kr.co.crevill.common.CommonService;
import kr.co.crevill.common.SessionUtil;
import kr.co.crevill.voucher.VoucherDto;
import kr.co.crevill.voucher.VoucherSaleDto;
import kr.co.crevill.voucher.VoucherService;
import kr.co.crevill.voucher.VoucherVo;

/**
 * 
 * @packageName : kr.co.crevill.member
 * @fileName : MemberController.java
 * @author : Juyoung Park
 * @date : 2021.02.10
 * @description :
 * ===========================================================
 * DATE AUTHOR NOTE * -----------------------------------------------------------
 * 2021.02.10 Juyoung Park 최초 생성
 */
@Controller
@RequestMapping("member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private VoucherService voucherService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping("welcome.view")
	public ModelAndView welcome(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/welcome");
		return mav;
	}
	
	@GetMapping("termsAgree.view")
	public ModelAndView termsAgree(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/termsAgree");
		return mav;
	}
	
	@GetMapping("join.view")
	public ModelAndView join(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/join");
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("LEARNING_GRADE");
		mav.addObject("learningGradeList", commonService.selectCommonCode(commonCodeDto));
		mav.addObject("sEncData", memberService.createNiceEncryptString(request));
		
		if(!"null".equals(String.valueOf(request.getParameter("niceSucc")))) {
			memberService.getNiceResult(request);
		}
		
		return mav;
	}
	
	@GetMapping("update.view")
	public ModelAndView update(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/update");
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("LEARNING_GRADE");
		mav.addObject("learningGradeList", commonService.selectCommonCode(commonCodeDto));
		mav.addObject("info", memberService.selectMemberInfo(memberDto));
		return mav;
	}
	
	@PostMapping("join.proc")
	@ResponseBody
	public JSONObject joinProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.insertMemberInfo(memberDto, request);
		return result;
	}
	
	@PostMapping("update.proc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request, @RequestBody MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.updateMemberInfo(memberDto, request);
		return result;
	}
	
	@PostMapping("checkMemberCellPhone.proc")
	@ResponseBody
	public JSONObject checkMemberCellPhone(HttpServletRequest request, @RequestBody MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.checkMemberCellPhone(memberDto);
		return result;
	}
	
	@GetMapping("info.view")
	public ModelAndView info(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/info");
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		memberDto.setQrCode(SessionUtil.getSessionMemberVo(request).getQrCode());
		VoucherSaleDto voucherSaleDto = new VoucherSaleDto();
		voucherSaleDto.setBuyCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		List<VoucherVo> voucherList = voucherService.getMemberVoucherList(voucherSaleDto);
		
		String memberGrade = "크레빌 회원";
		
		if(voucherList != null && voucherList.size() > 0) {
			memberGrade = "바우처 회원";
		} 
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("info", memberService.selectMemberInfo(memberDto));
		mav.addObject("memberGrade", memberGrade);
//		mav.addObject("voucherList", voucherList);
//		mav.addObject("promotionVoucherList", voucherService.selectPromotionVoucherList(voucherDto));
//		mav.addObject("visitList", memberService.selectVisitStoreList(memberDto));
//		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		return mav;
	}
	
	@GetMapping("voucherList.view")
	public ModelAndView voucherList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/voucherList");
		VoucherSaleDto voucherSaleDto = new VoucherSaleDto();
		voucherSaleDto.setBuyCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		List<VoucherVo> voucherList = voucherService.selectMemberVoucherInfo(voucherSaleDto);
		mav.addObject("voucherList", voucherList);
		return mav;
	}
	
	@GetMapping("visitList.view")
	public ModelAndView visitList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/visitList");
		MemberDto memberDto = new MemberDto();
		memberDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("visitList", memberService.selectVisitStoreList(memberDto));
		return mav;
	}
	
	@GetMapping("promotionVoucherList.view")
	public ModelAndView promotionVoucherList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/promotionVoucherList");
		VoucherDto voucherDto = new VoucherDto();
		voucherDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("promotionVoucherList", voucherService.selectPromotionVoucherList(voucherDto));
		return mav;
	}
	
	@GetMapping("childList.view")
	public ModelAndView childList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("member/childList");
		MemberDto memberDto = new MemberDto();
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("childList", memberService.selectChildMemberList(memberDto));
		return mav;
	}
	
	@GetMapping("childUpdate.view")
	public ModelAndView childUpdate(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/childUpdate");
		memberDto.setCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("LEARNING_GRADE");
		MemberVo memberInfo = memberService.selectChildMemberInfo(memberDto);
		mav.addObject("info", memberInfo);
		
		if(memberInfo.getLearningGrade() != null) {
			List<String> learningGradeList = new ArrayList<String>();
			for(String learningGrade : memberInfo.getLearningGrade().split(",")) {
				learningGradeList.add(learningGrade);
			}
			mav.addObject("checkedlearningGradeList", learningGradeList);
		}
		mav.addObject("learningGradeList", commonService.selectCommonCode(commonCodeDto));
		return mav;
	}
	
	@PostMapping("childUpdate.proc")
	@ResponseBody
	public JSONObject childUpdateProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.updateChildMemberInfo(memberDto, request);
		return result;
	}
	
	@PostMapping("childDelete.proc")
	@ResponseBody
	public JSONObject childDelete(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.childDelete(memberDto, request);
		return result;
	}
	
	@GetMapping("childAdd.view")
	public ModelAndView childAdd(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/childAdd");
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("LEARNING_GRADE");
		mav.addObject("learningGradeList", commonService.selectCommonCode(commonCodeDto));
		mav.addObject("cellPhone", SessionUtil.getSessionMemberVo(request).getCellPhone());
		mav.addObject("parentName", SessionUtil.getSessionMemberVo(request).getName());
		return mav;
	}
	
	@PostMapping("childAdd.proc")
	@ResponseBody
	public JSONObject childAddProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.childAdd(memberDto, request);
		return result;
	}
	
	@GetMapping("passwordChange.view")
	public ModelAndView passwordChange(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/passwordChange");
		memberDto.setParentCellPhone(SessionUtil.getSessionMemberVo(request).getCellPhone());
		MemberVo memberInfo = memberService.selectMemberInfo(memberDto);
		mav.addObject("password", memberInfo.getPassword());
		return mav;
	}
	
	@PostMapping("passwordChange.proc")
	@ResponseBody
	public JSONObject passwordChangeProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.passwordChange(memberDto, request);
		return result;
	}
	
	@GetMapping("privacyAgree.view")
	public ModelAndView privacyAgree(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/privacyAgree");
		return mav;
	}
	
	@GetMapping("terms.view")
	public ModelAndView terms(HttpServletRequest request, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView("member/terms");
		return mav;
	}
	
	@PostMapping("delete.proc")
	@ResponseBody
	public JSONObject deleteProc(HttpServletRequest request, @ModelAttribute MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result = memberService.deleteMemberInfo(memberDto);
		return result;
	}
	
	@GetMapping("memberOut.view")
	public ModelAndView memberOut(HttpServletRequest request, MemberDto memberDto) {
		HttpSession session = request.getSession();
		session.invalidate();
		ModelAndView mav = new ModelAndView("login/login");
		return mav;
	}
	
}