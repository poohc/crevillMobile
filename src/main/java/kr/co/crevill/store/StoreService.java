package kr.co.crevill.store;

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

@Service
public class StoreService {
	
	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private CommonMapper commonMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectStoreCount(StoreDto storeDto){
		return storeMapper.selectStoreCount(storeDto);
	}
	
	public List<StoreVo> selectStoreList(StoreDto storeDto){
		return storeMapper.selectStoreList(storeDto);
	}
	
	public List<StoreVo> selectShortVoucherStoreList(StoreDto storeDto){
		return storeMapper.selectShortVoucherStoreList(storeDto);
	}
	
	public StoreVo selectStoreInfo(StoreDto storeDto){
		return storeMapper.selectStoreInfo(storeDto);
	}
	
	/**
	 * 매장정보 저장
	 * @methodName : insertStore
	 * @author : Juyoung Park
	 * @date : 2021.03.03
	 * @param staffDto
	 * @return
	 */
	public JSONObject insertStore(StoreDto storeDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		//체험학습 가능 여부 체크가 되어 있지 않으면 무조건 N
		if(!"Y".equals(storeDto.getExperienceClass())) {
			storeDto.setExperienceClass("N");
		}
		
		if(storeDto.getRegistrationCertificate() != null && !storeDto.getRegistrationCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setRegistrationCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getRegistrationCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("사업자등록증");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getPlaygroundCertificate() != null && !storeDto.getPlaygroundCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setPlaygroundCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getPlaygroundCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("놀이시설인증서");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getInsuranceCertificate() != null && !storeDto.getInsuranceCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setInsuranceCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getInsuranceCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("보험증서");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile1() != null && !storeDto.getEtcFile1().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile1Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile1());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류1");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile2() != null && !storeDto.getEtcFile2().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile2Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile2());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류2");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile3() != null && !storeDto.getEtcFile3().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile3Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile3());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류3");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile4() != null && !storeDto.getEtcFile4().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile4Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile4());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류4");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getThumbnailImage() != null && !storeDto.getThumbnailImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setThumbnailImageIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getThumbnailImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장대표사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage1() != null && !storeDto.getImage1().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage1Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage1());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진1");
			commonMapper.insertImages(fileDto);
		}		
		
		if(storeDto.getImage2() != null && !storeDto.getImage2().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage2Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage2());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진2");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage3() != null && !storeDto.getImage3().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage3Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage3());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진3");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage4() != null && !storeDto.getImage4().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage4Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage4());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진4");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage5() != null && !storeDto.getImage5().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage5Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage5());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진5");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage6() != null && !storeDto.getImage6().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage6Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage6());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진6");
			commonMapper.insertImages(fileDto);
		}
		
		storeDto.setStoreId(storeMapper.selectStoreId());
		//직원정보 INSERT 
		if(storeMapper.insertStore(storeDto) > 0) {
			int storePlayCnt = storeDto.getStorePlay().split(",").length;
			int tmpCnt = 0;
			for(String playId : storeDto.getStorePlay().split(",")) {
				storeDto.setPlayId(playId);
				if(storeMapper.insertStorePlay(storeDto) > 0) {
					tmpCnt++;
				}
			}
			if(storePlayCnt == tmpCnt) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
			}
		}
		return result;
	}	
	
	public JSONObject updateStore(StoreDto storeDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		//체험학습 가능 여부 체크가 되어 있지 않으면 무조건 N
		if(!"Y".equals(storeDto.getExperienceClass())) {
			storeDto.setExperienceClass("N");
		}
		
		if(storeDto.getRegistrationCertificate() != null && !storeDto.getRegistrationCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setRegistrationCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getRegistrationCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("사업자등록증");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getPlaygroundCertificate() != null && !storeDto.getPlaygroundCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setPlaygroundCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getPlaygroundCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("놀이시설인증서");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getInsuranceCertificate() != null && !storeDto.getInsuranceCertificate().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setInsuranceCertificateIdx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getInsuranceCertificate());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("보험증서");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile1() != null && !storeDto.getEtcFile1().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile1Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile1());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류1");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile2() != null && !storeDto.getEtcFile2().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile2Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile2());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류2");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile3() != null && !storeDto.getEtcFile3().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile3Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile3());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류3");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getEtcFile4() != null && !storeDto.getEtcFile4().isEmpty()) {
			FileVo fileVo = commonMapper.selectFileIdx();
			storeDto.setEtcFile4Idx(fileVo.getFileIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getEtcFile4());
			fileDto.setFileIdx(fileVo.getFileIdx());
			fileDto.setDescription("기타서류4");
			commonMapper.insertFiles(fileDto);
		}
		
		if(storeDto.getThumbnailImage() != null && !storeDto.getThumbnailImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setThumbnailImageIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getThumbnailImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장대표사진");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage1() != null && !storeDto.getImage1().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage1Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage1());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진1");
			commonMapper.insertImages(fileDto);
		}		
		
		if(storeDto.getImage2() != null && !storeDto.getImage2().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage2Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage2());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진2");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage3() != null && !storeDto.getImage3().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage3Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage3());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진3");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage4() != null && !storeDto.getImage4().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage4Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage4());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진4");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage5() != null && !storeDto.getImage5().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage5Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage5());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진5");
			commonMapper.insertImages(fileDto);
		}
		
		if(storeDto.getImage6() != null && !storeDto.getImage6().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			storeDto.setImage6Idx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(storeDto.getImage6());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("매장사진6");
			commonMapper.insertImages(fileDto);
		}
		
		//직원정보 UPDATE
		if(storeMapper.updateStore(storeDto) > 0) {
			storeMapper.deleteStorePlay(storeDto);
			int storePlayCnt = storeDto.getStorePlay().split(",").length;
			int tmpCnt = 0;
			for(String playId : storeDto.getStorePlay().split(",")) {
				storeDto.setPlayId(playId);
				if(storeMapper.insertStorePlay(storeDto) > 0) {
					tmpCnt++;
				}
			}
			if(storePlayCnt == tmpCnt) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
			}
		}
		return result;
	}	
	
	public JSONObject deleteStore(StoreDto storeDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(storeMapper.deleteStorePlay(storeDto) > 0) {
			if(storeMapper.deleteStore(storeDto) > 0) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);
			}
		}
		return result;
	}
}