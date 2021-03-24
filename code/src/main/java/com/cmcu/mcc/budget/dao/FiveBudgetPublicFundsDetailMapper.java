package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetPublicFundsDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetPublicFundsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetPublicFundsDetail record);

    FiveBudgetPublicFundsDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetPublicFundsDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetPublicFundsDetail record);
}