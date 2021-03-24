package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccounts;
import java.util.List;
import java.util.Map;

public interface FiveFinanceTransferAccountsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceTransferAccounts record);

    FiveFinanceTransferAccounts selectByPrimaryKey(Integer id);

    List<FiveFinanceTransferAccounts> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceTransferAccounts record);
}