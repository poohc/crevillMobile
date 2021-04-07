package kr.co.crevill.staff;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StaffDto {
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
	private String storeId;
	private MultipartFile idPicture;
	private MultipartFile healthCertificate;
	private MultipartFile resume;
	private String idPictureIdx;
	private String healthCertificateIdx;
	private String resumeIdx;
	private String regId;
	private String updId;
	private String staffId;
	private String status;
	
	/* 페이징 관련 */
	private int currentPageNo;
	private int recordsPerPage;
	private int pageSize;
	private String searchKeyword;
	private String searchType;
	
	//페이징 초기 설정
	public StaffDto() {
		this.currentPageNo = 1;
		this.recordsPerPage = 10;
		this.pageSize = 10;
	}	
	
	public int getStartPage() {
		return (currentPageNo - 1) * recordsPerPage;
	}
}
