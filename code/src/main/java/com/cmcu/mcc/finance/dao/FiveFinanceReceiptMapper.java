package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceReceipt;
import java.util.List;
import java.util.Map;

public interface FiveFinanceReceiptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceReceipt record);

    FiveFinanceReceipt selectByPrimaryKey(Integer id);

    List<FiveFinanceReceipt> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceReceipt record);
}