package com.cmcu.mcc.budget.dao;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOutDetail;
import java.util.List;
import java.util.Map;

public interface FiveBudgetCapitalOutDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBudgetCapitalOutDetail record);

    FiveBudgetCapitalOutDetail selectByPrimaryKey(Integer id);

    List<FiveBudgetCapitalOutDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBudgetCapitalOutDetail record);
}