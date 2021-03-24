package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsInDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetScientificFundsInDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetScientificFundsInDetail record);

    FiveBudgetScientificFundsInDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetScientificFundsInDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetScientificFundsInDetail record);
}