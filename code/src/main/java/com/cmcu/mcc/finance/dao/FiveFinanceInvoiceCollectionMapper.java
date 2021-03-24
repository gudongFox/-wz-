package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCollection;
import java.util.List;
import java.util.Map;

public interface FiveFinanceInvoiceCollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceInvoiceCollection record);

    FiveFinanceInvoiceCollection selectByPrimaryKey(Integer id);

    List<FiveFinanceInvoiceCollection> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceInvoiceCollection record);
}