package kr.co.crevill.voucher;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class VoucherDto {
	private String voucherNo;
	private String grade;
	private String gradeType;
	private String ticketName;
	private String price;
	private String salePrice;
	private String useTime;
	private String endDate;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
	private String attribute;
	private MultipartFile image;
	private String imageIdx;
	private String status;
	private String storeId;
	private String cellPhone;
	private String reservationId;
	private String voucherUseId;
	private String scheduleId;
}