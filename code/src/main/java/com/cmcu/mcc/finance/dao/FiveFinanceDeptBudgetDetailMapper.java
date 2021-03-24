package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudgetDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceDeptBudgetDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceDeptBudgetDetail record);

    FiveFinanceDeptBudgetDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceDeptBudgetDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceDeptBudgetDetail record);
}