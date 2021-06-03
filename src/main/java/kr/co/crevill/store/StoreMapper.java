package kr.co.crevill.store;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreMapper {
	public String selectStoreId();
	public int selectStoreCount(StoreDto storeDto);
	public List<StoreVo> selectStoreList(StoreDto storeDto);
	public List<StoreVo> selectShortVoucherStoreList(StoreDto storeDto);
	public StoreVo selectStoreInfo(StoreDto storeDto);
	public int insertStore(StoreDto storeDto);
	public int insertStorePlay(StoreDto storeDto);
	public int updateStore(StoreDto storeDto);
	public int deleteStore(StoreDto storeDto);
	public int deleteStorePlay(StoreDto storeDto);
}