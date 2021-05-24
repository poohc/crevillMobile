package kr.co.crevill.program;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.crevill.common.CommonMapper;

@Service
public class ProgramService {

	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private ProgramMapper playMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public int selectPlayCount(ProgramDto playDto) {
		return playMapper.selectPlayCount(playDto);
	}
	
	public List<ProgramVo> selectPlayList(ProgramDto playDto){
		return playMapper.selectPlayList(playDto);
	}
	
	public ProgramVo selectPlayInfo(ProgramDto playDto) {
		return playMapper.selectPlayInfo(playDto);
	}
}