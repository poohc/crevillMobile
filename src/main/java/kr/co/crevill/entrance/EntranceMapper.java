package kr.co.crevill.entrance;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EntranceMapper {
	public List<EntranceVo> selectEntranceList(EntranceDto entranceDto);
	public EntranceVo selectEntranceInfo(EntranceDto entranceDto);
	public EntranceVo selectNonMemberScheduleList();
	public List<EntranceVo> selectNonMemberVoucherList(EntranceDto entranceDto);
	public EntranceVo checkVoucher(EntranceDto entranceDto);
	public int insertScheduleEntranceMember(EntranceDto entranceDto);
	public int insertVoucherUse(EntranceDto entranceDto);
}