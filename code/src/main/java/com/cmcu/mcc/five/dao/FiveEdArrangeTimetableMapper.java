package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdArrangeTimetable;
import java.util.List;
import java.util.Map;

public interface FiveEdArrangeTimetableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdArrangeTimetable record);

    FiveEdArrangeTimetable selectByPrimaryKey(Integer id);

    List<FiveEdArrangeTimetable> selectAll(Map params);

    int updateByPrimaryKey(FiveEdArrangeTimetable record);
}