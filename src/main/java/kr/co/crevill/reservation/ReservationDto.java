package kr.co.crevill.reservation;

import java.util.List;

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
	private String classType;
	private String operationType;
	private String playName;
	private String scheduleStart;
	private List<String> scheduleIdList;
	private String experienceClass;
	private String storeId;
}