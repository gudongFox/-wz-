package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetMaintainDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetMaintainDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetMaintainDetail record);

    FiveBudgetMaintainDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetMaintainDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetMaintainDetail record);
}