package kr.co.crevill.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberVo {
	private String name;
	private String cellPhone;
	private String email;
	private String address;
	private String roadAddress;
	private String detailAddress;
	private String parentName;
	private String parentCellPhone;
	private String childName;
	private String birthday;
	private String sex;
	private String learningGrade;
	private String qrCode;
	private String regDate;
	private String regId;
	private String updDate;
	private String updId;
	private String status;
	private String voucherCount;
	private String storeId;
	private String storeName;
	private String storeNameShort;
	private String password;
	private String pictureIdx;
	private String engName;
	private String visitDate;
	private String ticketName;
	private String useTime;
	private String playName;
	private String memberType;
}