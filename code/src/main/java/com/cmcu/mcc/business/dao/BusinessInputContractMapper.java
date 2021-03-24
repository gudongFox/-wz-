package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessInputContract;
import java.util.List;
import java.util.Map;

public interface BusinessInputContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessInputContract record);

    BusinessInputContract selectByPrimaryKey(Integer id);

    List<BusinessInputContract> selectAll(Map params);

    int updateByPrimaryKey(BusinessInputContract record);
}