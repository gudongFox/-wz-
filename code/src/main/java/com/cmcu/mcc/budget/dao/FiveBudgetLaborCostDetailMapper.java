package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetLaborCostDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetLaborCostDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetLaborCostDetail record);

    FiveBudgetLaborCostDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetLaborCostDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetLaborCostDetail record);
}