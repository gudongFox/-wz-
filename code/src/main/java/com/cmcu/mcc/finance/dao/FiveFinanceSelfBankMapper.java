package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceSelfBank;
import java.util.List;
import java.util.Map;

public interface FiveFinanceSelfBankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceSelfBank record);

    FiveFinanceSelfBank selectByPrimaryKey(Integer id);

    List<FiveFinanceSelfBank> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceSelfBank record);
}