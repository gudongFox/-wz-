package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsOut;
import java.util.List;
import java.util.Map;

public interface FiveBudgetScientificFundsOutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetScientificFundsOut record);

    FiveBudgetScientificFundsOut selectByPrimaryKey(Integer id);

    List<FiveBudgetScientificFundsOut> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetScientificFundsOut record);
}