package kr.co.crevill.voucher;

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
public class VoucherService {
	
	@Autowired
	private VoucherMapper voucherMapper;

	@Autowired
	private CommonMapper commonMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectVoucherCount(VoucherDto voucherDto) {
		return voucherMapper.selectVoucherCount(voucherDto);
	}
	
	public List<VoucherVo> selectVoucherList(VoucherDto voucherDto){
		return voucherMapper.selectVoucherList(voucherDto);
	}
	
	public VoucherVo selectVoucherInfo(VoucherDto voucherDto){
		return voucherMapper.selectVoucherInfo(voucherDto);
	}
	
	public List<VoucherVo> getVoucherList(VoucherDto voucherDto){
		return voucherMapper.getVoucherList(voucherDto);
	}
	
	public List<VoucherVo> selectVoucherAttributeList(VoucherDto voucherDto){
		return voucherMapper.selectVoucherAttributeList(voucherDto);
	}
	
	public VoucherVo selectVoucherTimeInfo(VoucherDto voucherDto) {
		return voucherMapper.selectVoucherTimeInfo(voucherDto);
	}
	
	public List<VoucherVo> getMemberVoucherList(VoucherSaleDto voucherSaleDto){
		return voucherMapper.getMemberVoucherList(voucherSaleDto);
	}
	
	public List<VoucherVo> getMemberVoucherAllList(VoucherDto voucherDto){
		return voucherMapper.getMemberVoucherAllList(voucherDto);
	}
	
	public List<VoucherVo> getMemberVoucherUseList(VoucherDto voucherDto){
		return voucherMapper.getMemberVoucherUseList(voucherDto);
	}
	
	public VoucherVo getMemberVoucherInfo(VoucherDto voucherDto) {
		return voucherMapper.getMemberVoucherInfo(voucherDto);
	}
	/**
	 * 직원 저장 처리 
	 * @methodName : insertVoucher
	 * @author : Juyoung Park
	 * @date : 2021.02.22
	 * @param voucherDto
	 * @return
	 */
	public JSONObject insertVoucher(VoucherDto voucherDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		voucherDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(voucherDto.getImage() != null && !voucherDto.getImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			voucherDto.setImageIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(voucherDto.getImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("바우처사진");
			commonMapper.insertImages(fileDto);
		}
		//VOUCHER_NO 설정
		voucherDto.setVoucherNo(voucherMapper.selectVoucherNo());
		
		//무제한일 경우 UNLIMIT INSERT
		if("0".equals(voucherDto.getUseTime())){ 
			voucherDto.setUseTime(CrevillConstants.VOUCHER_UNLIMITED_TIME);
		}
		
		if("0".equals(voucherDto.getEndDate())){ 
			voucherDto.setEndDate(CrevillConstants.VOUCHER_UNLIMITED_DATE);
		}
		
		voucherDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_READY);
		if(voucherMapper.insertVoucher(voucherDto) > 0) {
			//선택된 전달매체 모두 INSERT 성공해야 SUCC
			int cnt = voucherDto.getAttribute().split(",").length;
			int insCnt = 0;
			for(String attribute : voucherDto.getAttribute().split(",")) {
				voucherDto.setAttribute(attribute);
				if(voucherMapper.insertVoucherAttribute(voucherDto) > 0) {
					insCnt++;
				}	
			}
			if(cnt == insCnt) {
				result.put("resultCd", CrevillConstants.RESULT_SUCC);	
			}
		}
		return result;
	}	
	
	/**
	 * 바우처 수정처리
	 * @methodName : updateVoucher
	 * @author : Juyoung Park
	 * @date : 2021.03.24
	 * @param voucherDto
	 * @param request
	 * @return
	 */
	public JSONObject updateVoucher(VoucherDto voucherDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		voucherDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		voucherDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		
		if(voucherDto.getImage() != null && !voucherDto.getImage().isEmpty()) {
			FileVo fileVo = commonMapper.selectImagesIdx();
			voucherDto.setImageIdx(fileVo.getImageIdx());
			FileDto fileDto = CommonUtil.setBlobByMultiPartFile(voucherDto.getImage());
			fileDto.setImageIdx(fileVo.getImageIdx());
			fileDto.setDescription("바우처사진");
			commonMapper.insertImages(fileDto);
		}
		
		//무제한일 경우 UNLIMIT INSERT
		if("0".equals(voucherDto.getUseTime())){ 
			voucherDto.setUseTime(CrevillConstants.VOUCHER_UNLIMITED_TIME);
		}
		
		if("0".equals(voucherDto.getEndDate())){ 
			voucherDto.setEndDate(CrevillConstants.VOUCHER_UNLIMITED_DATE);
		}
		voucherDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_READY);
		
		//VOUCHER_ATTRIBUTE 테이블 데이터 먼저 삭제
		if(voucherMapper.deleteVoucherAttribute(voucherDto) > 0) {
			if(voucherMapper.updateVoucher(voucherDto) > 0) {
				//선택된 전달매체 모두 INSERT 성공해야 SUCC
				int cnt = voucherDto.getAttribute().split(",").length;
				int insCnt = 0;
				for(String attribute : voucherDto.getAttribute().split(",")) {
					voucherDto.setAttribute(attribute);
					if(voucherMapper.insertVoucherAttribute(voucherDto) > 0) {
						insCnt++;
					}	
				}
				if(cnt == insCnt) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);	
				}
			}
		}
		return result;
	}
	
	/**
	 * 바우처 삭제처리
	 * @methodName : updateVoucher
	 * @author : Juyoung Park
	 * @date : 2021.03.24
	 * @param voucherDto
	 * @return
	 */
	public JSONObject deleteVoucher(VoucherDto voucherDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
//		if(voucherMapper.deleteVoucherAttribute(voucherDto) > 0) {
//			if(voucherMapper.deleteVoucher(voucherDto) > 0) {
//				result.put("resultCd", CrevillConstants.RESULT_SUCC);
//			}
//		}
		voucherDto.setUpdId(SessionUtil.getSessionMemberVo(request).getQrCode());
		voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_CANCEL);
		if(voucherMapper.updateVoucher(voucherDto) > 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		
		return result;
	}
	
	/**
	 * VOUCHER_SALE 테이블 INSERT 처리
	 * @methodName : voucherSaleProc
	 * @author : Juyoung Park
	 * @date : 2021.03.08
	 * @param voucherSaleDto
	 * @param request
	 * @return
	 */
	public JSONObject voucherSaleProc(VoucherSaleDto voucherSaleDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		voucherSaleDto.setRegId(SessionUtil.getSessionMemberVo(request).getQrCode());
		voucherSaleDto.setStoreId(SessionUtil.getSessionMemberVo(request).getStoreId());
		result.put("resultCd", CrevillConstants.RESULT_FAIL);		
		if(!"undefined".equals(voucherSaleDto.getVoucherNo())) {
			if(voucherMapper.insertVoucherSale(voucherSaleDto) > 0) {
				VoucherDto voucherDto = new VoucherDto();
				voucherDto.setVoucherNo(voucherSaleDto.getVoucherNo());
				voucherDto.setStatus(CrevillConstants.VOUCHER_STATUS_SALE);
				if(voucherMapper.updateVoucher(voucherDto) > 0) {
					result.put("resultCd", CrevillConstants.RESULT_SUCC);	
				}
			}
		}
		return result;
	}	
}