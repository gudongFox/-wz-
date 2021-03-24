package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationPlanDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationPlanDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationPlanDetail record);

    FiveOaInformationPlanDetail selectByPrimaryKey(Integer id);

    List<FiveOaInformationPlanDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationPlanDetail record);
}