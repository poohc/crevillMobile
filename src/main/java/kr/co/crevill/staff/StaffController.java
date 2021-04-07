package kr.co.crevill.staff;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import kr.co.crevill.store.StoreDto;
import kr.co.crevill.store.StoreService;

@Controller
@RequestMapping("staff")
public class StaffController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StaffService staffService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, StaffDto staffDto) {
		ModelAndView mav = new ModelAndView("staff/list");
		mav.addObject("staffCount", staffService.selectStaffCount(staffDto));
		mav.addObject("staffList", staffService.selectStaffList(staffDto));
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView regist(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("staff/regist");
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("WORKER_TYPE");
		mav.addObject("workerType", commonService.selectCommonCode(commonCodeDto));
		commonCodeDto.setCodeType("STAFF_GRADE");
		mav.addObject("staffGrade", commonService.selectCommonCode(commonCodeDto));
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@GetMapping("update.view")
	public ModelAndView update(HttpServletRequest request, StaffDto staffDto) {
		ModelAndView mav = new ModelAndView("staff/update");
		CommonCodeDto commonCodeDto = new CommonCodeDto();
		commonCodeDto.setCodeType("WORKER_TYPE");
		mav.addObject("workerType", commonService.selectCommonCode(commonCodeDto));
		commonCodeDto.setCodeType("STAFF_GRADE");
		mav.addObject("staffGrade", commonService.selectCommonCode(commonCodeDto));
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		mav.addObject("info", staffService.selectStaffInfo(staffDto));
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute StaffDto staffDto) {
		JSONObject result = new JSONObject();
		result = staffService.insertStaffInfo(staffDto, request);
		return result;
	}
	
	@PostMapping("update.proc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request, @ModelAttribute StaffDto staffDto) {
		JSONObject result = new JSONObject();
		result = staffService.updateStaffInfo(staffDto, request);
		return result;
	}
	
	@PostMapping("updateEnd.proc")
	@ResponseBody
	public JSONObject updateEndProc(HttpServletRequest request, @ModelAttribute StaffDto staffDto) {
		JSONObject result = new JSONObject();
		result = staffService.updateEnd(staffDto, request);
		return result;
	}
	
	@GetMapping("nsList.view")
	public ModelAndView nsList(HttpServletRequest request, InstructorDto instructorDto) {
		ModelAndView mav = new ModelAndView("staff/nsList");
		mav.addObject("list", staffService.selectInstructorList(instructorDto));
		return mav;
	}
	
	@GetMapping("nsRegist.view")
	public ModelAndView nsRegist(HttpServletRequest request, InstructorDto instructorDto) {
		ModelAndView mav = new ModelAndView("staff/nsRegist");
		mav.addObject("nationalCode", commonService.selectNationalCode());
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		return mav;
	}
	
	@GetMapping("nsUpdate.view")
	public ModelAndView nsUpdate(HttpServletRequest request, InstructorDto instructorDto) {
		ModelAndView mav = new ModelAndView("staff/nsUpdate");
		mav.addObject("nationalCode", commonService.selectNationalCode());
		StoreDto storeDto = new StoreDto();
		mav.addObject("storeList", storeService.selectStoreList(storeDto));
		mav.addObject("info", staffService.selectInstructorInfo(instructorDto));
		return mav;
	}
	
	@PostMapping("nsRegist.proc")
	@ResponseBody
	public JSONObject nsRegistProc(HttpServletRequest request, @ModelAttribute InstructorDto instructorDto) {
		JSONObject result = new JSONObject();
		result = staffService.insertInstructorInfo(instructorDto);
		return result;
	}
	
	@PostMapping("checkInstructorTelNo.proc")
	@ResponseBody
	public JSONObject checkInstructorTelNo(HttpServletRequest request, @RequestBody InstructorDto instructorDto) {
		JSONObject result = new JSONObject();
		result = staffService.checkInstructorTelNo(instructorDto);
		return result;
	}
	
	@PostMapping("nsDelete.proc")
	@ResponseBody
	public JSONObject nsDelete(HttpServletRequest request, @ModelAttribute InstructorDto instructorDto) {
		JSONObject result = new JSONObject();
		result = staffService.nsDelete(instructorDto, request);
		return result;
	}
	
	@PostMapping("nsUpdate.proc")
	@ResponseBody
	public JSONObject nsUpdateProc(HttpServletRequest request, @ModelAttribute InstructorDto instructorDto) {
		JSONObject result = new JSONObject();
		result = staffService.updateNstaffInfo(instructorDto, request);
		return result;
	}
}