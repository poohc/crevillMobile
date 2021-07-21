package kr.co.crevill.branches;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchesService {

	@Autowired
	private BranchesMapper branchesMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<NoticeVo> selectNoticeList(NoticeDto noticeDto){
		return branchesMapper.selectNoticeList(noticeDto);
	}
	
	public NoticeVo selectNoticeInfo(NoticeDto noticeDto) {
		return branchesMapper.selectNoticeInfo(noticeDto);
	}
}