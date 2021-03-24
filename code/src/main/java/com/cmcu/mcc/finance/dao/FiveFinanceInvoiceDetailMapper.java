package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceInvoiceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceInvoiceDetail record);

    FiveFinanceInvoiceDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceInvoiceDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceInvoiceDetail record);
}