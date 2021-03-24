package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaScientificTaskCostApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaScientificTaskCostApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaScientificTaskCostApplyDetail record);

    FiveOaScientificTaskCostApplyDetail selectByPrimaryKey(Integer id);

    List<FiveOaScientificTaskCostApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaScientificTaskCostApplyDetail record);
}