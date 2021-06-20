package kr.co.crevill.member;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberDto {
	private String name;
	private String cellPhone;
	private String email;
	private String address;
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
	private String storeId;
	private String voucherCount;
	private String storeName;
	private String password;
	private MultipartFile picture;
	private String pictureIdx;
	private String engName;
	private String parentBirthday;
	private String parentSex;
	private String mobileCorp;
	private String di;
	private String authNum;
}