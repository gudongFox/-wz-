package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessCustomer;
import java.util.List;
import java.util.Map;

public interface BusinessCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessCustomer record);

    BusinessCustomer selectByPrimaryKey(Integer id);

    List<BusinessCustomer> selectAll(Map params);

    int updateByPrimaryKey(BusinessCustomer record);
}