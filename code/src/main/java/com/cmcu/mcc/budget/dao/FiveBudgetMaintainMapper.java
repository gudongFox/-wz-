package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetMaintain;
import java.util.List;
import java.util.Map;

public interface FiveBudgetMaintainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetMaintain record);

    FiveBudgetMaintain selectByPrimaryKey(Integer id);

    List<FiveBudgetMaintain> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetMaintain record);
}