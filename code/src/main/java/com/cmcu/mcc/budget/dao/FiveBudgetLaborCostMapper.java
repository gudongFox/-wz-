package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetLaborCost;
import java.util.List;
import java.util.Map;

public interface FiveBudgetLaborCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetLaborCost record);

    FiveBudgetLaborCost selectByPrimaryKey(Integer id);

    List<FiveBudgetLaborCost> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetLaborCost record);
}