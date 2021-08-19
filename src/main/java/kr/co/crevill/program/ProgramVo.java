package kr.co.crevill.program;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ProgramVo {
	private String storeId;
	private String day;
	private String viewDay;
	private String time;
	private String dayoftheweek;
	private String playName;
	private String programName;
	private String month;
}