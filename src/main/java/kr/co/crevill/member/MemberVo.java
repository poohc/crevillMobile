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
	private String password;
}