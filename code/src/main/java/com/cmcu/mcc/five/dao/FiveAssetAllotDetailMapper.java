package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveAssetAllotDetail;
import java.util.List;
import java.util.Map;

public interface FiveAssetAllotDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveAssetAllotDetail record);

    FiveAssetAllotDetail selectByPrimaryKey(Integer id);

    List<FiveAssetAllotDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveAssetAllotDetail record);
}