package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessChangeCustomer;
import java.util.List;
import java.util.Map;

public interface BusinessChangeCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessChangeCustomer record);

    BusinessChangeCustomer selectByPrimaryKey(Integer id);

    List<BusinessChangeCustomer> selectAll(Map params);

    int updateByPrimaryKey(BusinessChangeCustomer record);
}