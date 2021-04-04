package kr.co.crevill.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReservationDto {
	private String reservationId;
	private String cellPhone;
	private String voucherNo;
	private String scheduleId;
	private String status;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
	private String childName;
	private String tutoringYn;
	private String voucherUseId;
}