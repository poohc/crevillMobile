package kr.co.crevill.reservation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.crevill.schedule.ScheduleDto;

@Mapper
@Repository
public interface ReservationMapper {
	public String selectReservationId();
	public int selectReservationCount(ScheduleDto scheduleDto);
	public List<ReservationVo> selectReservationList(ScheduleDto scheduleDto);
	public List<ReservationVo> selectReservationSearchList(ScheduleDto scheduleDto);
	public int checkAlreadyReservation(ReservationDto reservationDto);
	public int checkAlreadyFreeReservation(ReservationDto reservationDto); 
	public ReservationVo checkReservationYn(ReservationDto reservationDto);
	public ReservationVo checkTutoringReservationYn(ReservationDto reservationDto);
	public ReservationVo checkVoucherYn(ReservationDto reservationDto);
	public ReservationVo selectReservationPlayInfo(ReservationDto reservationDto);
	public int insertReservation(ReservationDto reservationDto);
	public int updateReservation(ReservationDto reservationDto);
	public List<ReservationVo> selectRecommendReservationWeekday();
	public List<ReservationVo> selectRecommendReservationWeekend();
	public List<ReservationVo> selectQuickReservation(ScheduleDto scheduleDto);
	public List<ReservationVo> selectAvaReservation(ReservationDto reservationDto);
	public List<ReservationVo> selectSearchDayReservation(ReservationDto reservationDto);
	public int checkAlreadyShortReservation(ReservationDto reservationDto);
}