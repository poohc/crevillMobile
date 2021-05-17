package kr.co.crevill.promotion;

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
public class PromotionService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private PromotionMapper promotionMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<PromotionVo> selectPromotionList(PromotionDto promotionDto){
		return promotionMapper.selectPromotionList(promotionDto);
	}
	
	public List<PromotionVo> getPromotionList(PromotionDto promotionDto){
		return promotionMapper.getPromotionList(promotionDto);
	}
	
	public PromotionVo selectPromotionInfo(PromotionDto promotionDto) {
		return promotionMapper.selectPromotionInfo(promotionDto);
	}
	
	public JSONObject insertPromotion(PromotionDto promotionDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(promotionDto.getPromotionBanner() != null && !promotionDto.getPromotionBanner().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			promotionDto.setPromotionBannerId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(promotionDto.getPromotionBanner());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("프로모션 배너이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(promotionDto.getPromotionDetailImage() != null && !promotionDto.getPromotionDetailImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			promotionDto.setPromotionDetailImageId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(promotionDto.getPromotionDetailImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("프로모션 상세이미지");
			commonMapper.insertImages(fileDto);
		}
		
		promotionDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		promotionDto.setPromotionId(promotionMapper.selectPromotionIdSeq());
		promotionDto.setStatus(CrevillConstants.PROMOTION_STATUS_ACTIVE);
		//프로모션 정보 INSERT
		if(promotionMapper.insertPromotion(promotionDto) > 0) {
			int insertCount = 0;
			int originCount = promotionDto.getStoreId().split(",").length;
			for(String storeId : promotionDto.getStoreId().split(",")) {
				promotionDto.setStoreId(storeId);
				if(promotionMapper.insertPromotionStore(promotionDto) > 0) insertCount++;
			}
			
			if(insertCount == originCount) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);	
			}
		}
		return result;
	}
	
	public JSONObject updatePromotion(PromotionDto promotionDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(promotionDto.getPromotionBanner() != null && !promotionDto.getPromotionBanner().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			promotionDto.setPromotionBannerId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(promotionDto.getPromotionBanner());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("프로모션 배너이미지");
			commonMapper.insertImages(fileDto);
		}
		
		if(promotionDto.getPromotionDetailImage() != null && !promotionDto.getPromotionDetailImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			promotionDto.setPromotionDetailImageId(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(promotionDto.getPromotionDetailImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("프로모션 상세이미지");
			commonMapper.insertImages(fileDto);
		}
		
		promotionDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		if(promotionMapper.updatePromotion(promotionDto) > 0) {
			//프로모션 매장정보 삭제 후 INSERT
			if(promotionMapper.deletePromotionStore(promotionDto) > 0) {
				int insertCount = 0;
				int originCount = promotionDto.getStoreId().split(",").length;
				for(String storeId : promotionDto.getStoreId().split(",")) {
					promotionDto.setStoreId(storeId);
					if(promotionMapper.insertPromotionStore(promotionDto) > 0) insertCount++;
				}
				if(insertCount == originCount) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);	
				}
			}
		}
		return result;
	}
	
	/**
	 * 프로모션 중지 처리 
	 * @methodName : stopPromotion
	 * @author : Juyoung Park
	 * @date : 2021.05.12
	 * @param promotionDto
	 * @return
	 */
	public JSONObject stopPromotion(PromotionDto promotionDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		promotionDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		promotionDto.setStatus(CrevillConstants.PROMOTION_STATUS_INACTIVE);
		if(promotionMapper.updatePromotion(promotionDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	/**
	 * 프로모션 삭제 처리
	 * @methodName : deletePromotion
	 * @author : Juyoung Park
	 * @date : 2021.05.12
	 * @param promotionDto
	 * @return
	 */
	public JSONObject deletePromotion(PromotionDto promotionDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(promotionMapper.deletePromotionStore(promotionDto) > 0) {
			if(promotionMapper.deletePromotion(promotionDto) > 0) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
			}
		}
		return result;
	}
	
}