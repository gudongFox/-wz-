package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsOutDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetScientificFundsOutDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetScientificFundsOutDetail record);

    FiveBudgetScientificFundsOutDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetScientificFundsOutDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetScientificFundsOutDetail record);
}