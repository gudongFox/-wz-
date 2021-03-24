package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaGeneralCountersign;
import java.util.List;
import java.util.Map;

public interface FiveOaGeneralCountersignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaGeneralCountersign record);

    FiveOaGeneralCountersign selectByPrimaryKey(Integer id);

    List<FiveOaGeneralCountersign> selectAll(Map params);

    int updateByPrimaryKey(FiveOaGeneralCountersign record);
}