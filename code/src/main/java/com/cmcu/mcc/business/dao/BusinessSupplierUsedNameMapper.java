package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessSupplierUsedName;
import java.util.List;
import java.util.Map;

public interface BusinessSupplierUsedNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessSupplierUsedName record);

    BusinessSupplierUsedName selectByPrimaryKey(Integer id);

    List<BusinessSupplierUsedName> selectAll(Map params);

    int updateByPrimaryKey(BusinessSupplierUsedName record);
}