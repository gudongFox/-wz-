package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationPlan;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationPlan record);

    FiveOaInformationPlan selectByPrimaryKey(Integer id);

    List<FiveOaInformationPlan> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationPlan record);
}