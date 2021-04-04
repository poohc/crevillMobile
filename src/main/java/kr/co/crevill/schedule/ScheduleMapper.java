package kr.co.crevill.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ScheduleMapper {
	public int selectScheduleCount(ScheduleDto scheduleDto);
	public List<ScheduleVo> selectScheduleList(ScheduleDto scheduleDto);
	public int insertSchedule(ScheduleDto scheduleDto);
}