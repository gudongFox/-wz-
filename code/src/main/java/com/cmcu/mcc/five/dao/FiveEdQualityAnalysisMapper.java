package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdQualityAnalysis;
import java.util.List;
import java.util.Map;

public interface FiveEdQualityAnalysisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdQualityAnalysis record);

    FiveEdQualityAnalysis selectByPrimaryKey(Integer id);

    List<FiveEdQualityAnalysis> selectAll(Map params);

    int updateByPrimaryKey(FiveEdQualityAnalysis record);
}