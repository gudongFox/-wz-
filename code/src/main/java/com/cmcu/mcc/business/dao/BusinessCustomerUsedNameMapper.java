package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessCustomerUsedName;
import java.util.List;
import java.util.Map;

public interface BusinessCustomerUsedNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessCustomerUsedName record);

    BusinessCustomerUsedName selectByPrimaryKey(Integer id);

    List<BusinessCustomerUsedName> selectAll(Map params);

    int updateByPrimaryKey(BusinessCustomerUsedName record);
}