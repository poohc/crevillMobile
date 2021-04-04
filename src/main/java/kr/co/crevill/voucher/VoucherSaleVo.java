package kr.co.crevill.voucher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class VoucherSaleVo {
	private String saleNo;
	private String voucherNo;
	private String buyCellPhone;
	private String usedChildrenName;
	private String pgType;
	private String approvalNo;
	private String storeId;
	private String regId;
	private String regDate;
	private String updId;
	private String updDate;
	
	/* 페이징 관련 */
	private int currentPageNo;
	private int recordsPerPage;
	private int pageSize;
	private String searchKeyword;
	private String searchType;
	
	//페이징 초기 설정
	public VoucherSaleVo() {
		this.currentPageNo = 1;
		this.recordsPerPage = 10;
		this.pageSize = 10;
	}	
	
	public int getStartPage() {
		return (currentPageNo - 1) * recordsPerPage;
	}
}
