package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceOutSupply;
import java.util.List;
import java.util.Map;

public interface FiveFinanceOutSupplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceOutSupply record);

    FiveFinanceOutSupply selectByPrimaryKey(Integer id);

    List<FiveFinanceOutSupply> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceOutSupply record);
}