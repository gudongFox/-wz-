package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessBidApply;
import java.util.List;
import java.util.Map;

public interface BusinessBidApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessBidApply record);

    BusinessBidApply selectByPrimaryKey(Integer id);

    List<BusinessBidApply> selectAll(Map params);

    int updateByPrimaryKey(BusinessBidApply record);

    List<String> listAgent();
}