package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import java.util.List;
import java.util.Map;

public interface FiveOaInlandProjectApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInlandProjectApply record);

    FiveOaInlandProjectApply selectByPrimaryKey(Integer id);

    List<FiveOaInlandProjectApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInlandProjectApply record);
}