package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCancel;
import java.util.List;
import java.util.Map;

public interface FiveFinanceInvoiceCancelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceInvoiceCancel record);

    FiveFinanceInvoiceCancel selectByPrimaryKey(Integer id);

    List<FiveFinanceInvoiceCancel> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceInvoiceCancel record);
}