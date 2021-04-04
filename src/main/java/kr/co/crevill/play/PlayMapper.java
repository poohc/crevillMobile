package kr.co.crevill.play;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlayMapper {
	public int selectPlayCount(PlayDto playDto);
	public List<PlayVo> selectPlayList(PlayDto playDto);
	public PlayVo selectPlayInfo(PlayDto playDto);
	public int insertPlay(PlayDto playDto);
	public int updatePlay(PlayDto playDto);
	public int deletePlay(PlayDto playDto);
}