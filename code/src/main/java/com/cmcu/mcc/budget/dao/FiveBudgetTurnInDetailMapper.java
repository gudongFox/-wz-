package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetTurnInDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetTurnInDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetTurnInDetail record);

    FiveBudgetTurnInDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetTurnInDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetTurnInDetail record);
}