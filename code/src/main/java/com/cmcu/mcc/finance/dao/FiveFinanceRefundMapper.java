package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceRefund;
import java.util.List;
import java.util.Map;

public interface FiveFinanceRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceRefund record);

    FiveFinanceRefund selectByPrimaryKey(Integer id);

    List<FiveFinanceRefund> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceRefund record);
}