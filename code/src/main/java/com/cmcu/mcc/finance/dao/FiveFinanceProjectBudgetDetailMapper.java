package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudgetDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceProjectBudgetDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceProjectBudgetDetail record);

    FiveFinanceProjectBudgetDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceProjectBudgetDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceProjectBudgetDetail record);
}