package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysConfig;
import java.util.List;
import java.util.Map;

public interface SysConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    List<SysConfig> selectAll(Map params);

    int updateByPrimaryKey(SysConfig record);
}