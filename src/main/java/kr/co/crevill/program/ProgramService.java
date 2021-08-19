package kr.co.crevill.program;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {
	
	@Autowired
	private ProgramMapper programMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectTimeListCount(ProgramDto programDto) {
		return programMapper.selectTimeListCount(programDto);
	}
	
	public List<ProgramVo> selectTimeTitleList(ProgramDto programDto){
		return programMapper.selectTimeTitleList(programDto);
	}
	
	public List<ProgramVo> selectTimeList(ProgramDto programDto){
		return programMapper.selectTimeList(programDto);
	}
}