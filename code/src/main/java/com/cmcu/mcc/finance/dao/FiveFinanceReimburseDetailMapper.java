package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceReimburseDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceReimburseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceReimburseDetail record);

    FiveFinanceReimburseDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceReimburseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceReimburseDetail record);
}