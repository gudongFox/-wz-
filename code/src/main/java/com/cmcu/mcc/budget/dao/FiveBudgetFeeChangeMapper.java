package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetFeeChange;
import java.util.List;
import java.util.Map;

public interface FiveBudgetFeeChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetFeeChange record);

    FiveBudgetFeeChange selectByPrimaryKey(Integer id);

    List<FiveBudgetFeeChange> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetFeeChange record);
}