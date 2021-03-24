package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessSupplier;
import java.util.List;
import java.util.Map;

public interface BusinessSupplierMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessSupplier record);

    BusinessSupplier selectByPrimaryKey(Integer id);

    List<BusinessSupplier> selectAll(Map params);

    int updateByPrimaryKey(BusinessSupplier record);
}