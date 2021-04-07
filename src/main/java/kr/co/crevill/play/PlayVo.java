package kr.co.crevill.play;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PlayVo {
	private String playId;
	private String name;
	private String description;
	private String playTime;
	private String operationType;
	private String thumbnailIdx;
	private String pictureIdx;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
}