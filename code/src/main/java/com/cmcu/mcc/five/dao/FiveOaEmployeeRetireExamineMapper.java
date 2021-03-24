package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEmployeeRetireExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaEmployeeRetireExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEmployeeRetireExamine record);

    FiveOaEmployeeRetireExamine selectByPrimaryKey(Integer id);

    List<FiveOaEmployeeRetireExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEmployeeRetireExamine record);
}