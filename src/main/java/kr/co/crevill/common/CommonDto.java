package kr.co.crevill.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CommonDto {
	private String voucherTimeLeftHour;
	private String reservationCount;
	private String memberCount;
	private String todayJoinMemberCount;
	private String todayVoucherSalePrice;
	private String yesterdayVoucherSalePrice;
	private String lastweekVoucherSalePrice;
	private String lastTwWeekVoucherSalePrice;
	private String lastmonthVoucherSalePrice;
	private String lastTwMonthVoucherSalePrice;
	private String lastyearVoucherSalePrice;
	private String lastTwYearVoucherSalePrice;
	private String todayArrow;
	private String weekArrow;
	private String monthArrow;
	private String yearArrow;
	private String ticketName;
	private String salePer;
	private String reservationName;
	private String reservationDate;
	private String reservationTime;
	private String reservationStatus;
	private String cellPhone;
	private String storeId;
}