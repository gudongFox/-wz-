package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysAclModule;
import java.util.List;
import java.util.Map;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    List<SysAclModule> selectAll(Map params);

    int updateByPrimaryKey(SysAclModule record);
}