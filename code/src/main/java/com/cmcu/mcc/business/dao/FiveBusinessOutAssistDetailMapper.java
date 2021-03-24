package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessOutAssistDetail;
import java.util.List;
import java.util.Map;

public interface FiveBusinessOutAssistDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessOutAssistDetail record);

    FiveBusinessOutAssistDetail selectByPrimaryKey(Integer id);

    List<FiveBusinessOutAssistDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessOutAssistDetail record);
}