package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import java.util.List;
import java.util.Map;

public interface FiveFinanceStampTaxMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceStampTax record);

    FiveFinanceStampTax selectByPrimaryKey(Integer id);

    List<FiveFinanceStampTax> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceStampTax record);
}