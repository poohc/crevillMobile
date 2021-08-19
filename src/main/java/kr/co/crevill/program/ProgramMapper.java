package kr.co.crevill.program;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProgramMapper {
	public int selectTimeListCount(ProgramDto programDto);
	public List<ProgramVo> selectTimeTitleList(ProgramDto programDto);	
	public List<ProgramVo> selectTimeList(ProgramDto programDto);	
}