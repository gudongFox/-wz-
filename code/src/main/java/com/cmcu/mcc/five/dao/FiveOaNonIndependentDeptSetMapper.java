package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaNonIndependentDeptSet;
import java.util.List;
import java.util.Map;

public interface FiveOaNonIndependentDeptSetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaNonIndependentDeptSet record);

    FiveOaNonIndependentDeptSet selectByPrimaryKey(Integer id);

    List<FiveOaNonIndependentDeptSet> selectAll(Map params);

    int updateByPrimaryKey(FiveOaNonIndependentDeptSet record);
}