package kr.co.crevill.branches;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NoticeDto {
	private String noticeId;
	private String title;
	private String noticeType;
	private String noticeSendType;
	private String contents;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
	private String startDate;
	private String endDate;
	private int limit;
}
