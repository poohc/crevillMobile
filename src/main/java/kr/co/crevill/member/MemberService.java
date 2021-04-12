package kr.co.crevill.member;

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
public class MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private CommonMapper commonMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	public MemberVo selectMemberInfo(MemberDto memberDto) {
		return memberMapper.selectMemberInfo(memberDto);
	}
	
	public List<MemberVo> selectChildMemberList(MemberDto memberDto){
		return memberMapper.selectChildMemberList(memberDto);
	}
	
	public JSONObject checkMemberCellPhone(MemberDto memberDto) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		if(memberMapper.checkExistCellPhone(memberDto) == 0) {
			result.put("resultCd", CrevillConstants.RESULT_SUCC);
		}
		return result;
	}
	
	public JSONObject insertMemberInfo(MemberDto memberDto, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		result.put("resultCd", CrevillConstants.RESULT_FAIL);
		memberDto.setRegId(CrevillConstants.REG_ID_MOBILE);
		memberDto.setStoreId(CrevillConstants.STORE_ID_MOBILE);
		//부모 정보 INSERT 성공 시 자녀정보 INSERT
		if(memberMapper.insertMemberParent(memberDto) > 0) {
			//자녀정보 INSERT 성공 시 resultCd = SUCC
			
			if(memberDto.getPicture() != null && !memberDto.getPicture().isEmpty()) {
				FileVo fileVo = commonMapper.selectImagesIdx();
				memberDto.setPictureIdx(fileVo.getImageIdx());
				FileDto fileDto = CommonUtil.setBlobByMultiPartFile(memberDto.getPicture());
				fileDto.setImageIdx(fileVo.getImageIdx());
				fileDto.setDescription("아이사진");
				commonMapper.insertImages(fileDto);
			}
			
			if(memberMapper.insertMemberChildren(memberDto) > 0) {
				//자녀 영어수준 INSERT 추가
				int learningGradeCount = memberDto.getLearningGrade().split(",").length;
				int tmpCnt = 0;
				
				if(learningGradeCount > 0) {
					for(String learningGrade : memberDto.getLearningGrade().split(",")) {
						memberDto.setLearningGrade(learningGrade);
						if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
							tmpCnt++;
						}
					}
					if(learningGradeCount == tmpCnt) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				} else {
					if(memberMapper.insertMemberChildrenGrade(memberDto) > 0) {
						result.put("resultCd", CrevillConstants.RESULT_SUCC);
					}
				}
			}
		}
		return result;
	}
}