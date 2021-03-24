package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import java.util.List;
import java.util.Map;

public interface FiveBudgetCapitalOutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetCapitalOut record);

    FiveBudgetCapitalOut selectByPrimaryKey(Integer id);

    List<FiveBudgetCapitalOut> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetCapitalOut record);
}