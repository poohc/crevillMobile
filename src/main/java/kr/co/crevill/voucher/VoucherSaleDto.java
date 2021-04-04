package kr.co.crevill.voucher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class VoucherSaleDto {
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
}
