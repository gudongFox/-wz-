package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaStampApplyOffice;
import java.util.List;
import java.util.Map;

public interface FiveOaStampApplyOfficeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaStampApplyOffice record);

    FiveOaStampApplyOffice selectByPrimaryKey(Integer id);

    List<FiveOaStampApplyOffice> selectAll(Map params);

    int updateByPrimaryKey(FiveOaStampApplyOffice record);
}