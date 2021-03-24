package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessContract;
import java.util.List;
import java.util.Map;

public interface BusinessContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessContract record);

    BusinessContract selectByPrimaryKey(Integer id);

    List<BusinessContract> selectAll(Map params);

    int updateByPrimaryKey(BusinessContract record);

    List<BusinessContract> selectNumByDeptIdAndTime(Map params);
}