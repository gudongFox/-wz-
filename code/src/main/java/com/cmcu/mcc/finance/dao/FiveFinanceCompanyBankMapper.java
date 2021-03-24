package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceCompanyBank;
import java.util.List;
import java.util.Map;

public interface FiveFinanceCompanyBankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceCompanyBank record);

    FiveFinanceCompanyBank selectByPrimaryKey(Integer id);

    List<FiveFinanceCompanyBank> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceCompanyBank record);
}