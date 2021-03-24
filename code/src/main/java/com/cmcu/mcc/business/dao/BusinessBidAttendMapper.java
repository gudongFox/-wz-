package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessBidAttend;
import java.util.List;
import java.util.Map;

public interface BusinessBidAttendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessBidAttend record);

    BusinessBidAttend selectByPrimaryKey(Integer id);

    List<BusinessBidAttend> selectAll(Map params);

    int updateByPrimaryKey(BusinessBidAttend record);
}