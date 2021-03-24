package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudget;
import java.util.List;
import java.util.Map;

public interface FiveFinanceDeptBudgetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceDeptBudget record);

    FiveFinanceDeptBudget selectByPrimaryKey(Integer id);

    List<FiveFinanceDeptBudget> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceDeptBudget record);
}