package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdTask;
import java.util.List;
import java.util.Map;

public interface FiveEdTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdTask record);

    FiveEdTask selectByPrimaryKey(Integer id);

    List<FiveEdTask> selectAll(Map params);

    int updateByPrimaryKey(FiveEdTask record);
}