package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpenseDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceTravelExpenseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceTravelExpenseDetail record);

    FiveFinanceTravelExpenseDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceTravelExpenseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceTravelExpenseDetail record);
}