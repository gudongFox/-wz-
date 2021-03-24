package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetFee;
import java.util.List;
import java.util.Map;

public interface FiveBudgetFeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetFee record);

    FiveBudgetFee selectByPrimaryKey(Integer id);

    List<FiveBudgetFee> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetFee record);
}