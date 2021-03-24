package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependentDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetIndependentDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetIndependentDetail record);

    FiveBudgetIndependentDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetIndependentDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetIndependentDetail record);
}