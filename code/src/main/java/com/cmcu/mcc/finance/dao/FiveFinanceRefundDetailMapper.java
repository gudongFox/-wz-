package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceRefundDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceRefundDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceRefundDetail record);

    FiveFinanceRefundDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceRefundDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceRefundDetail record);
}