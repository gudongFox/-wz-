package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdArrangePlan;
import java.util.List;
import java.util.Map;

public interface FiveEdArrangePlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdArrangePlan record);

    FiveEdArrangePlan selectByPrimaryKey(Integer id);

    List<FiveEdArrangePlan> selectAll(Map params);

    int updateByPrimaryKey(FiveEdArrangePlan record);
}