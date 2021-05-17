package kr.co.crevill.store;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StoreDto {
	private String storeId;
	private String playId;
	private String playName;
	private String storeName;
	private String storeNameReg;
	private String storeType;
	private String storePlay;
	private String storeStatus;
	private String telNo;
	private String sortRank;
	private String registrationNumber;
	private String ceo;
	private String address;
	private String openDay;
	private String closeDay;
	private String experienceClass;
	private String location;
	private MultipartFile registrationCertificate;
	private MultipartFile playgroundCertificate;
	private MultipartFile insuranceCertificate;
	private MultipartFile etcFile1;
	private MultipartFile etcFile2;
	private MultipartFile etcFile3;
	private MultipartFile etcFile4;
	private MultipartFile thumbnailImage;
	private MultipartFile image1;
	private MultipartFile image2;
	private MultipartFile image3;
	private MultipartFile image4;
	private MultipartFile image5;
	private MultipartFile image6;
	private String registrationCertificateIdx;
	private String playgroundCertificateIdx;
	private String insuranceCertificateIdx;
	private String etcFile1Idx;
	private String etcFile2Idx;
	private String etcFile3Idx;
	private String etcFile4Idx;
	private String thumbnailImageIdx;
	private String image1Idx;
	private String image2Idx;
	private String image3Idx;
	private String image4Idx;
	private String image5Idx;
	private String image6Idx;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;	
}