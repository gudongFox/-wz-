package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessOutAssist;
import java.util.List;
import java.util.Map;

public interface FiveBusinessOutAssistMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessOutAssist record);

    FiveBusinessOutAssist selectByPrimaryKey(Integer id);

    List<FiveBusinessOutAssist> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessOutAssist record);
}