package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import java.util.List;
import java.util.Map;

public interface FiveFinanceLoanDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceLoanDetail record);

    FiveFinanceLoanDetail selectByPrimaryKey(Integer id);

    List<FiveFinanceLoanDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceLoanDetail record);
}