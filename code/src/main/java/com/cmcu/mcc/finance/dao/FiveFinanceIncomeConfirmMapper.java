package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceIncomeConfirm;
import java.util.List;
import java.util.Map;

public interface FiveFinanceIncomeConfirmMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceIncomeConfirm record);

    FiveFinanceIncomeConfirm selectByPrimaryKey(Integer id);

    List<FiveFinanceIncomeConfirm> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceIncomeConfirm record);
}