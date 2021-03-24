package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessChangeSupplier;
import java.util.List;
import java.util.Map;

public interface BusinessChangeSupplierMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessChangeSupplier record);

    BusinessChangeSupplier selectByPrimaryKey(Integer id);

    List<BusinessChangeSupplier> selectAll(Map params);

    int updateByPrimaryKey(BusinessChangeSupplier record);
}