package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceReimburse;
import java.util.List;
import java.util.Map;

public interface FiveFinanceReimburseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceReimburse record);

    FiveFinanceReimburse selectByPrimaryKey(Integer id);

    List<FiveFinanceReimburse> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceReimburse record);
}