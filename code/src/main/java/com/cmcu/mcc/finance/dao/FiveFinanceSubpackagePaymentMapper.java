package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceSubpackagePayment;
import java.util.List;
import java.util.Map;

public interface FiveFinanceSubpackagePaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceSubpackagePayment record);

    FiveFinanceSubpackagePayment selectByPrimaryKey(Integer id);

    List<FiveFinanceSubpackagePayment> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceSubpackagePayment record);
}