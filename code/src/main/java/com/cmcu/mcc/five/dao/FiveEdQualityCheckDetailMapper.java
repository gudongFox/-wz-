package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdQualityCheckDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdQualityCheckDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdQualityCheckDetail record);

    FiveEdQualityCheckDetail selectByPrimaryKey(Integer id);

    List<FiveEdQualityCheckDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdQualityCheckDetail record);
}