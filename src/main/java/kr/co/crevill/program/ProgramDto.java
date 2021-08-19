package kr.co.crevill.program;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ProgramDto {
	private String storeId;
	private String searchMonth;
	private String day;
	private String time;
	private String dayoftheweek;
	private String playName;
	private List<String> playNameList;
	private List<String> playNameListNext;
	private String programName;
	private String month;
}