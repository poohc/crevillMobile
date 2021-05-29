package kr.co.crevill.storeProgram;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.crevill.play.PlayDto;
import kr.co.crevill.play.PlayService;

@Controller
@RequestMapping("storeProgram")
public class StoreProgramController {
	
	@Autowired
	private StoreProgramService storeProgramService;
	
	@Autowired
	private PlayService playService;
	
	@GetMapping("list.view")
	public ModelAndView list(HttpServletRequest request, @ModelAttribute StoreProgramDto storeProgramDto) {
		ModelAndView mav = new ModelAndView("storeProgram/list");
		List<StoreProgramVo> list = storeProgramService.selectStoreProgramList(storeProgramDto);
		mav.addObject("playName", list.get(0).getChainClassName());
		mav.addObject("list", list);
		return mav;
	}
	
	@GetMapping("info.view")
	public ModelAndView info(HttpServletRequest request, @ModelAttribute StoreProgramDto storeProgramDto) {
		ModelAndView mav = new ModelAndView("storeProgram/info");
		StoreProgramVo info = storeProgramService.selectStoreProgramInfo(storeProgramDto);
		mav.addObject("info", info);
		mav.addObject("playName", info.getChainClassName());
		return mav;
	}
	
	@GetMapping("regist.view")
	public ModelAndView master(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storeProgram/regist");
		PlayDto playDto = new PlayDto();
		mav.addObject("playList", playService.selectPlayList(playDto));
		return mav;
	}
	
	@PostMapping("regist.proc")
	@ResponseBody
	public JSONObject registProc(HttpServletRequest request, @ModelAttribute StoreProgramDto storeProgramDto) {
		JSONObject result = new JSONObject();
		result = storeProgramService.insertStoreProgram(storeProgramDto, request);
		return result;
	}
	
	@GetMapping("update.view")
	public ModelAndView master(HttpServletRequest request, StoreProgramDto storeProgramDto) {
		ModelAndView mav = new ModelAndView("storeProgram/update");
		PlayDto playDto = new PlayDto();
		mav.addObject("playList", playService.selectPlayList(playDto));
		mav.addObject("info", storeProgramService.selectStoreProgramInfo(storeProgramDto));
		return mav;
	}
	
	@PostMapping("update.proc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request, @ModelAttribute StoreProgramDto storeProgramDto) {
		JSONObject result = new JSONObject();
		result = storeProgramService.updateStoreProgram(storeProgramDto, request);
		return result;
	}
	
	@PostMapping("stop.proc")
	@ResponseBody
	public JSONObject stopProc(HttpServletRequest request, @ModelAttribute StoreProgramDto storeProgramDto) {
		JSONObject result = new JSONObject();
		result = storeProgramService.stopStoreProgram(storeProgramDto);
		return result;
	}
}