package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEmployeeDimissionExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaEmployeeDimissionExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEmployeeDimissionExamine record);

    FiveOaEmployeeDimissionExamine selectByPrimaryKey(Integer id);

    List<FiveOaEmployeeDimissionExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEmployeeDimissionExamine record);
}