package kr.co.crevill.branches;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class NoticeVo {
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
	private String bannerId;
}
