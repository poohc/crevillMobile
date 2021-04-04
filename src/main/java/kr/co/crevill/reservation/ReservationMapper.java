package kr.co.crevill.reservation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.crevill.schedule.ScheduleDto;

@Mapper
@Repository
public interface ReservationMapper {
	public int selectReservationCount(ScheduleDto scheduleDto);
	public List<ReservationVo> selectReservationList(ScheduleDto scheduleDto);
	public List<ReservationVo> selectReservationSearchList(ScheduleDto scheduleDto);
	public int checkAlreadyReservation(ReservationDto reservationDto);
	public ReservationVo checkReservationYn(ReservationDto reservationDto);
	public ReservationVo checkTutoringReservationYn(ReservationDto reservationDto);
	public ReservationVo checkVoucherYn(ReservationDto reservationDto);
	public ReservationVo selectReservationPlayInfo(ReservationDto reservationDto);
	public int insertReservation(ReservationDto reservationDto);
	public int updateReservation(ReservationDto reservationDto);
}