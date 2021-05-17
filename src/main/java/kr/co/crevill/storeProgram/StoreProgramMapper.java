package kr.co.crevill.storeProgram;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreProgramMapper {
	public List<StoreProgramVo> selectStoreProgramList(StoreProgramDto storeProgramDto);
	public StoreProgramVo selectStoreProgramInfo(StoreProgramDto storeProgramDto);
	public int insertStoreProgram(StoreProgramDto storeProgramDto);
	public int updateStoreProgram(StoreProgramDto storeProgramDto);
	public int deleteStoreProgram(StoreProgramDto storeProgramDto);
}