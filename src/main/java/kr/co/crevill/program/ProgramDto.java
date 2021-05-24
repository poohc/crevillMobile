package kr.co.crevill.program;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ProgramDto {
	private String programId;
	private String storeId;
	private String storeName;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
}