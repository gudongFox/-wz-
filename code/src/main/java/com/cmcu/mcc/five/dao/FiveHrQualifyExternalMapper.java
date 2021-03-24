package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveHrQualifyExternal;
import java.util.List;
import java.util.Map;

public interface FiveHrQualifyExternalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveHrQualifyExternal record);

    FiveHrQualifyExternal selectByPrimaryKey(Integer id);

    List<FiveHrQualifyExternal> selectAll(Map params);

    int updateByPrimaryKey(FiveHrQualifyExternal record);
}