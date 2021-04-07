package kr.co.crevill.staff;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class InstructorDto {
	private String nsId;
	private String nationality;
	private String name;
	private String fullName;
	private String telNo;
	private String originTelNo;
	private String address;
	private String roadAddress;
	private String detailAddress;
	private String startDate;
	private String storeId;
	private MultipartFile picture;
	private MultipartFile criminalRecords;
	private MultipartFile resume;
	private String pictureIdx;
	private String criminalRecordsIdx;
	private String resumeIdx;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
}