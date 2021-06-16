package kr.co.crevill.staff;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StaffVo {
	private String name;
	private String nameEng;
	private String telNo;
	private String address;
	private String roadAddress;
	private String detailAddress;
	private String startDate;
	private String endDate;
	private String workerType;
	private String staffGrade;
	private String staffGradeName;
	private String storeId;
	private String storeName;
	private byte[] idPicture;
	private byte[] healthCertificate;
	private byte[] resume;
	private String idPictureIdx;
	private String healthCertificateIdx;
	private String resumeIdx;
	private String regId;
	private String updId;
	private String staffId;
	private String status;
}
