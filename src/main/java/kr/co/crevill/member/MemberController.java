package kr.co.crevill.member;

import javax.servlet.http.HttpServletRequest;

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
import kr.co.crevill.voucher.VoucherSaleDto;
import kr.co.crevill.voucher.VoucherService;

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
		mav.addObject("info", memberService.selectMemberInfo(memberDto));
		mav.addObject("voucherList", voucherService.getMemberVoucherList(voucherSaleDto));
		mav.addObject("visitList", memberService.selectVisitStoreList(memberDto));
		return mav;
	}
}