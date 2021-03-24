package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessSupplierAptitude;
import java.util.List;
import java.util.Map;

public interface BusinessSupplierAptitudeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessSupplierAptitude record);

    BusinessSupplierAptitude selectByPrimaryKey(Integer id);

    List<BusinessSupplierAptitude> selectAll(Map params);

    int updateByPrimaryKey(BusinessSupplierAptitude record);
}