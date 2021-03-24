package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessSubpackage;
import java.util.List;
import java.util.Map;

public interface BusinessSubpackageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessSubpackage record);

    BusinessSubpackage selectByPrimaryKey(Integer id);

    List<BusinessSubpackage> selectAll(Map params);

    int updateByPrimaryKey(BusinessSubpackage record);
}