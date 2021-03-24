package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessBidContract;
import java.util.List;
import java.util.Map;

public interface BusinessBidContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessBidContract record);

    BusinessBidContract selectByPrimaryKey(Integer id);

    List<BusinessBidContract> selectAll(Map params);

    int updateByPrimaryKey(BusinessBidContract record);
}