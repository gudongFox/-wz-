package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysRole;
import java.util.List;
import java.util.Map;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    List<SysRole> selectAll(Map params);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> listRoleByUserId(int userId);
}