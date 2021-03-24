package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetTurnIn;
import java.util.List;
import java.util.Map;

public interface FiveBudgetTurnInMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetTurnIn record);

    FiveBudgetTurnIn selectByPrimaryKey(Integer id);

    List<FiveBudgetTurnIn> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetTurnIn record);
}