package kr.co.crevill.play;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
public class PlayService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private PlayMapper playMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectPlayCount(PlayDto playDto) {
		return playMapper.selectPlayCount(playDto);
	}
	
	public List<PlayVo> selectPlayList(PlayDto playDto){
		return playMapper.selectPlayList(playDto);
	}
	
	public PlayVo selectPlayInfo(PlayDto playDto) {
		return playMapper.selectPlayInfo(playDto);
	}
	
	public JSONObject insertPlay(PlayDto playDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(playDto.getThumbnail() != null && !playDto.getThumbnail().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			playDto.setThumbnailIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(playDto.getThumbnail());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("플레이 썸네일 이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(playDto.getPicture() != null && !playDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			playDto.setPictureIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(playDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("플레이 이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(playDto.getOperationType().split(",").length > 1) {
			playDto.setOperationType("BOTH");
		}
		
		playDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		//직원정보 INSERT 
		if(playMapper.insertPlay(playDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject updatePlay(PlayDto playDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(playDto.getThumbnail() != null && !playDto.getThumbnail().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			playDto.setThumbnailIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(playDto.getThumbnail());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("플레이 썸네일 이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(playDto.getPicture() != null && !playDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			playDto.setPictureIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(playDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("플레이 이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(playDto.getOperationType().split(",").length > 1) {
			playDto.setOperationType("BOTH");
		}
		
		playDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		//직원정보 INSERT 
		if(playMapper.updatePlay(playDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject deletePlay(PlayDto playDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(playMapper.deletePlay(playDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
}