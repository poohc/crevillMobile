package kr.co.crevill.promotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PromotionVo {
	private String promotionId;
	private String promotionName;
	private String promotionContents;
	private String endDate;
	private String promotionType;
	private String promotionValue;
	private String promotionBannerId;
	private String promotionDetailImageId;
	private String storeId;
	private String status;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
}