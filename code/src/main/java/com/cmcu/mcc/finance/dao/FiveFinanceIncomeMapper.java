package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import java.util.List;
import java.util.Map;

public interface FiveFinanceIncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceIncome record);

    FiveFinanceIncome selectByPrimaryKey(Integer id);

    List<FiveFinanceIncome> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceIncome record);
}