package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceStampTaxDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceStampTaxDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceStampTaxDetail record);

    FiveFinanceStampTaxDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceStampTaxDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceStampTaxDetail record);
}