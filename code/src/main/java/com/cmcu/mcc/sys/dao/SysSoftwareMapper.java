package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysSoftware;
import java.util.List;
import java.util.Map;

public interface SysSoftwareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSoftware record);

    SysSoftware selectByPrimaryKey(Integer id);

    List<SysSoftware> selectAll(Map params);

    int updateByPrimaryKey(SysSoftware record);
}