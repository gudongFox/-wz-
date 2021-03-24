package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessAdvance;
import java.util.List;
import java.util.Map;

public interface FiveBusinessAdvanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessAdvance record);

    FiveBusinessAdvance selectByPrimaryKey(Integer id);

    List<FiveBusinessAdvance> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessAdvance record);
}