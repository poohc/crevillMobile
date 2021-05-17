package kr.co.crevill.promotion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PromotionMapper {
	public List<PromotionVo> selectPromotionList(PromotionDto promotionDto);
	public List<PromotionVo> getPromotionList(PromotionDto promotionDto);
	public PromotionVo selectPromotionInfo(PromotionDto promotionDto);
	public String selectPromotionIdSeq();
	public int insertPromotion(PromotionDto promotionDto);
	public int insertPromotionStore(PromotionDto promotionDto);
	public int updatePromotion(PromotionDto promotionDto);
	public int deletePromotion(PromotionDto promotionDto);
	public int deletePromotionStore(PromotionDto promotionDto);
}