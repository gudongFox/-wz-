package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetFeeDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetFeeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetFeeDetail record);

    FiveBudgetFeeDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetFeeDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetFeeDetail record);
}