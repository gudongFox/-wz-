package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpense;
import java.util.List;
import java.util.Map;

public interface FiveFinanceTravelExpenseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceTravelExpense record);

    FiveFinanceTravelExpense selectByPrimaryKey(Integer id);

    List<FiveFinanceTravelExpense> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceTravelExpense record);
}