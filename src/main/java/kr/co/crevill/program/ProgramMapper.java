package kr.co.crevill.program;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProgramMapper {
	public int selectPlayCount(ProgramDto playDto);
	public List<ProgramVo> selectPlayList(ProgramDto playDto);
	public ProgramVo selectPlayInfo(ProgramDto playDto);
	public int insertPlay(ProgramDto playDto);
	public int updatePlay(ProgramDto playDto);
	public int deletePlay(ProgramDto playDto);
}