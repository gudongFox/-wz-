package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessRecord;
import java.util.List;
import java.util.Map;

public interface BusinessRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessRecord record);

    BusinessRecord selectByPrimaryKey(Integer id);

    List<BusinessRecord> selectAll(Map params);

    int updateByPrimaryKey(BusinessRecord record);
}