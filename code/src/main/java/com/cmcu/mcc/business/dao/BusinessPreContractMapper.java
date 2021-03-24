package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessPreContract;
import java.util.List;
import java.util.Map;

public interface BusinessPreContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessPreContract record);

    BusinessPreContract selectByPrimaryKey(Integer id);

    List<BusinessPreContract> selectAll(Map params);

    int updateByPrimaryKey(BusinessPreContract record);
}