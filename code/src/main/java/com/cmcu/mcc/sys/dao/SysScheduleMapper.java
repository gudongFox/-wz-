package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysSchedule;
import java.util.List;
import java.util.Map;

public interface SysScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSchedule record);

    SysSchedule selectByPrimaryKey(Integer id);

    List<SysSchedule> selectAll(Map params);

    int updateByPrimaryKey(SysSchedule record);
}