package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceTransferAccountsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceTransferAccountsDetail record);

    FiveFinanceTransferAccountsDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceTransferAccountsDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceTransferAccountsDetail record);
}