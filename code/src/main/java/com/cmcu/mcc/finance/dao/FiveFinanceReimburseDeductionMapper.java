package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceReimburseDeduction;
import java.util.List;
import java.util.Map;

public interface FiveFinanceReimburseDeductionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceReimburseDeduction record);

    FiveFinanceReimburseDeduction selectByPrimaryKey(Integer id);

    List<FiveFinanceReimburseDeduction> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceReimburseDeduction record);
}