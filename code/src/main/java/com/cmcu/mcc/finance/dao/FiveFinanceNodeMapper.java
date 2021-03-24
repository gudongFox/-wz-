package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceNode;
import java.util.List;
import java.util.Map;

public interface FiveFinanceNodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceNode record);

    FiveFinanceNode selectByPrimaryKey(Integer id);

    List<FiveFinanceNode> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceNode record);
}