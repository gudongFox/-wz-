package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysPlugin;
import java.util.List;
import java.util.Map;

public interface SysPluginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPlugin record);

    SysPlugin selectByPrimaryKey(Integer id);

    List<SysPlugin> selectAll(Map params);

    int updateByPrimaryKey(SysPlugin record);

    SysPlugin getLatest();
}