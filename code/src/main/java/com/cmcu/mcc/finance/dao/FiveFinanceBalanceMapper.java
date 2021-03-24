package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceBalance;
import java.util.List;
import java.util.Map;

public interface FiveFinanceBalanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceBalance record);

    FiveFinanceBalance selectByPrimaryKey(Integer id);

    List<FiveFinanceBalance> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceBalance record);
}