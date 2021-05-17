package kr.co.crevill.storeProgram;

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
public class StoreProgramService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private StoreProgramMapper storeProgramMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<StoreProgramVo> selectStoreProgramList(StoreProgramDto storeProgramDto){
		return storeProgramMapper.selectStoreProgramList(storeProgramDto);
	}
	
	public StoreProgramVo selectStoreProgramInfo(StoreProgramDto storeProgramDto) {
		return storeProgramMapper.selectStoreProgramInfo(storeProgramDto);
	}
	
	public JSONObject insertStoreProgram(StoreProgramDto storeProgramDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		storeProgramDto.setStatus(CrevillConstants.PROGRAM_STATUS_ACTIVE);
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(storeProgramDto.getThumbnail() != null && !storeProgramDto.getThumbnail().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setThumbnailId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getThumbnail());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 썸네일");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeProgramDto.getPicture() != null && !storeProgramDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setPictureId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeProgramDto.getTeachingPlanImg() != null && !storeProgramDto.getTeachingPlanImg().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setTeachingPlanImgId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getTeachingPlanImg());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 교안");
			commonMapper.insertImages(fileDto);
		}
		
		storeProgramDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		//프로모션 정보 INSERT
		if(storeProgramMapper.insertStoreProgram(storeProgramDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject updateStoreProgram(StoreProgramDto storeProgramDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(storeProgramDto.getThumbnail() != null && !storeProgramDto.getThumbnail().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setThumbnailId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getThumbnail());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 썸네일");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeProgramDto.getPicture() != null && !storeProgramDto.getPicture().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setPictureId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getPicture());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeProgramDto.getTeachingPlanImg() != null && !storeProgramDto.getTeachingPlanImg().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeProgramDto.setTeachingPlanImgId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeProgramDto.getTeachingPlanImg());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장 프로그램 교안");
			commonMapper.insertImages(fileDto);
		}
		
		storeProgramDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		if(storeProgramMapper.updateStoreProgram(storeProgramDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);	
		}
		return result;
	}
	
	
	public JSONObject stopStoreProgram(StoreProgramDto storeProgramDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		storeProgramDto.setStatus(CrevillConstants.PROGRAM_STATUS_INACTIVE);
		if(storeProgramMapper.updateStoreProgram(storeProgramDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);	
		}
		return result;
	}
	
}