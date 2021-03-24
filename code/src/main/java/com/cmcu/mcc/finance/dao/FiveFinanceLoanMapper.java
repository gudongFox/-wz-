package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import java.util.List;
import java.util.Map;

public interface FiveFinanceLoanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceLoan record);

    FiveFinanceLoan selectByPrimaryKey(Integer id);

    List<FiveFinanceLoan> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceLoan record);
}