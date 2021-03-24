package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsIn;
import java.util.List;
import java.util.Map;

public interface FiveBudgetScientificFundsInMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetScientificFundsIn record);

    FiveBudgetScientificFundsIn selectByPrimaryKey(Integer id);

    List<FiveBudgetScientificFundsIn> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetScientificFundsIn record);
}