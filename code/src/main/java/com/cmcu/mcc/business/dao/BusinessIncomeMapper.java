package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessIncome;
import java.util.List;
import java.util.Map;

public interface BusinessIncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessIncome record);

    BusinessIncome selectByPrimaryKey(Integer id);

    List<BusinessIncome> selectAll(Map params);

    int updateByPrimaryKey(BusinessIncome record);
}