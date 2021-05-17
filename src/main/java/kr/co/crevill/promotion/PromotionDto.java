package kr.co.crevill.promotion;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PromotionDto {
	private String promotionId;
	private String promotionName;
	private String promotionContents;
	private String endDate;
	private String promotionType;
	private String promotionValue;
	private MultipartFile promotionBanner;
	private MultipartFile promotionDetailImage;
	private String promotionBannerId;
	private String promotionDetailImageId;
	private String storeId;
	private String status;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
}