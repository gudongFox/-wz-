package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaScientificTaskCostApply;
import java.util.List;
import java.util.Map;

public interface FiveOaScientificTaskCostApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaScientificTaskCostApply record);

    FiveOaScientificTaskCostApply selectByPrimaryKey(Integer id);

    List<FiveOaScientificTaskCostApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaScientificTaskCostApply record);
}