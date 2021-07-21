package kr.co.crevill.branches;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("branches")
public class BranchesController {
	
	@Autowired
	private BranchesService branchesService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("noticeList.view")
	public ModelAndView noticeList(HttpServletRequest request, NoticeDto noticeDto) {
		ModelAndView mav = new ModelAndView("branches/noticeList");
		mav.addObject("list", branchesService.selectNoticeList(noticeDto));
		return mav;
	}
	
	@GetMapping("noticeInfo.view")
	public ModelAndView noticeInfo(HttpServletRequest request, NoticeDto noticeDto) {
		ModelAndView mav = new ModelAndView("branches/noticeInfo");
		mav.addObject("info", branchesService.selectNoticeInfo(noticeDto));
		return mav;
	}
}