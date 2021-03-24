package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdQualityCheck;
import java.util.List;
import java.util.Map;

public interface FiveEdQualityCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdQualityCheck record);

    FiveEdQualityCheck selectByPrimaryKey(Integer id);

    List<FiveEdQualityCheck> selectAll(Map params);

    int updateByPrimaryKey(FiveEdQualityCheck record);
}