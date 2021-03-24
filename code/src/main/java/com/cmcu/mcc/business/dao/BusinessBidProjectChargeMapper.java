package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessBidProjectCharge;
import java.util.List;
import java.util.Map;

public interface BusinessBidProjectChargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessBidProjectCharge record);

    BusinessBidProjectCharge selectByPrimaryKey(Integer id);

    List<BusinessBidProjectCharge> selectAll(Map params);

    int updateByPrimaryKey(BusinessBidProjectCharge record);
}