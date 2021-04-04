package kr.co.crevill.voucher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class VoucherVo {
	private String voucherNo;
	private String grade;
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
	private String imageIdx;
	private String status;
	private String voucherTimeLeftHour;
	private String storeId;
	private String storeName;
	private String name;
	private String childName;
	private String playName;
	private String useTutoringTime;
	private String reservationId;
	private String cellPhone;
	private String timeLeftHour;
    private String timeLeftMinute;
    private String voucherUseId; 
}
