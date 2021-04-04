package kr.co.crevill.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ScheduleDto {
	private String scheduleId;
	private String operationType;
	private String playId;
	private String numberOfPeople;
	private String tutoringNumber;
	private String scheduleType;
	private String scheduleStart;
	private String scheduleEnd;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
	private String storeId;
	private String subTopic;
	private String classType;
	private String cellPhone;
}