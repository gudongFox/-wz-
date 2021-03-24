package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudget;
import java.util.List;
import java.util.Map;

public interface FiveFinanceProjectBudgetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceProjectBudget record);

    FiveFinanceProjectBudget selectByPrimaryKey(Integer id);

    List<FiveFinanceProjectBudget> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceProjectBudget record);
}