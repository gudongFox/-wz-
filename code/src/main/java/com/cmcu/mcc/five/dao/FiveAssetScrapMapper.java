package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveAssetScrap;
import java.util.List;
import java.util.Map;

public interface FiveAssetScrapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveAssetScrap record);

    FiveAssetScrap selectByPrimaryKey(Integer id);

    List<FiveAssetScrap> selectAll(Map params);

    int updateByPrimaryKey(FiveAssetScrap record);
}