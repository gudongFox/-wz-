package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveAssetAllot;
import java.util.List;
import java.util.Map;

public interface FiveAssetAllotMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveAssetAllot record);

    FiveAssetAllot selectByPrimaryKey(Integer id);

    List<FiveAssetAllot> selectAll(Map params);

    int updateByPrimaryKey(FiveAssetAllot record);
}