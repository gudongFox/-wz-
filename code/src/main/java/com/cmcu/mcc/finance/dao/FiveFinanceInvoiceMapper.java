package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import java.util.List;
import java.util.Map;

public interface FiveFinanceInvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceInvoice record);

    FiveFinanceInvoice selectByPrimaryKey(Integer id);

    List<FiveFinanceInvoice> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceInvoice record);
}