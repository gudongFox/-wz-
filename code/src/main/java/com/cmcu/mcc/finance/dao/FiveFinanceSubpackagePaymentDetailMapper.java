package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceSubpackagePaymentDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceSubpackagePaymentDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceSubpackagePaymentDetail record);

    FiveFinanceSubpackagePaymentDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceSubpackagePaymentDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceSubpackagePaymentDetail record);
}