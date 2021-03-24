package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptFund;
import java.util.List;
import java.util.Map;

public interface FiveFinanceDeptFundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceDeptFund record);

    FiveFinanceDeptFund selectByPrimaryKey(Integer id);

    List<FiveFinanceDeptFund> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceDeptFund record);
}