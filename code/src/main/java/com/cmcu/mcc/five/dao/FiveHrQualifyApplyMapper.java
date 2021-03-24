package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveHrQualifyApply;
import java.util.List;
import java.util.Map;

public interface FiveHrQualifyApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveHrQualifyApply record);

    FiveHrQualifyApply selectByPrimaryKey(Integer id);

    List<FiveHrQualifyApply> selectAll(Map params);

    int updateByPrimaryKey(FiveHrQualifyApply record);
}