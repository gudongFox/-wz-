package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveAssetScrapDetail;
import java.util.List;
import java.util.Map;

public interface FiveAssetScrapDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveAssetScrapDetail record);

    FiveAssetScrapDetail selectByPrimaryKey(Integer id);

    List<FiveAssetScrapDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveAssetScrapDetail record);
}