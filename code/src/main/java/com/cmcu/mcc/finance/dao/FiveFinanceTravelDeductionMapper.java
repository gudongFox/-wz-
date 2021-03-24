package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelDeduction;
import java.util.List;
import java.util.Map;

public interface FiveFinanceTravelDeductionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceTravelDeduction record);

    FiveFinanceTravelDeduction selectByPrimaryKey(Integer id);

    List<FiveFinanceTravelDeduction> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceTravelDeduction record);
}