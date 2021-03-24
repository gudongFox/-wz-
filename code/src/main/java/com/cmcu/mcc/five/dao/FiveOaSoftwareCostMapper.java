package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaSoftwareCost;
import java.util.List;
import java.util.Map;

public interface FiveOaSoftwareCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSoftwareCost record);

    FiveOaSoftwareCost selectByPrimaryKey(Integer id);

    List<FiveOaSoftwareCost> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSoftwareCost record);
}