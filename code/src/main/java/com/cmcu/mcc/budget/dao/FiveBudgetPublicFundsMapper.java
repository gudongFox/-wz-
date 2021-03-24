package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetPublicFunds;
import java.util.List;
import java.util.Map;

public interface FiveBudgetPublicFundsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetPublicFunds record);

    FiveBudgetPublicFunds selectByPrimaryKey(Integer id);

    List<FiveBudgetPublicFunds> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetPublicFunds record);
}