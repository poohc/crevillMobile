package kr.co.crevill.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VoucherMapper {
	public int selectVoucherCount(VoucherDto voucherDto);
	public String selectVoucherNo();
	public List<VoucherVo> selectVoucherList(VoucherDto voucherDto);
	public VoucherVo selectVoucherInfo(VoucherDto voucherDto);
	public List<VoucherVo> getVoucherList(VoucherDto voucherDto);
	public List<VoucherVo> selectVoucherAttributeList(VoucherDto voucherDto);
	public VoucherVo selectVoucherTimeInfo(VoucherDto voucherDto);
	public List<VoucherVo> getMemberVoucherList(VoucherSaleDto voucherSaleDto);
	public List<VoucherVo> getMemberVoucherAllList(VoucherDto voucherDto);
	public List<VoucherVo> getMemberVoucherUseList(VoucherDto voucherDto);
	public int insertVoucher(VoucherDto voucherDto);
	public int insertVoucherAttribute(VoucherDto voucherDto);
	public int insertVoucherSale(VoucherSaleDto voucherSaleDto);
	public int updateVoucher(VoucherDto voucherDto);
	public int updateVoucherSale(VoucherSaleDto voucherSaleDto);
	public int updateVoucherUse(VoucherDto voucherDto);
	public int deleteVoucher(VoucherDto voucherDto);
	public int deleteVoucherAttribute(VoucherDto voucherDto);
}