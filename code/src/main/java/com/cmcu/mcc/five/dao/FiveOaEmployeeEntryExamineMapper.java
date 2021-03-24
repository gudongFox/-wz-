package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEmployeeEntryExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaEmployeeEntryExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEmployeeEntryExamine record);

    FiveOaEmployeeEntryExamine selectByPrimaryKey(Integer id);

    List<FiveOaEmployeeEntryExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEmployeeEntryExamine record);
}