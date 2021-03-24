package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependent;
import java.util.List;
import java.util.Map;

public interface FiveBudgetIndependentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetIndependent record);

    FiveBudgetIndependent selectByPrimaryKey(Integer id);

    List<FiveBudgetIndependent> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetIndependent record);
}