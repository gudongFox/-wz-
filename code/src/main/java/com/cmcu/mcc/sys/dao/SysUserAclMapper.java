package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysUserAcl;
import java.util.List;
import java.util.Map;

public interface SysUserAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserAcl record);

    SysUserAcl selectByPrimaryKey(Integer id);

    List<SysUserAcl> selectAll(Map params);

    int updateByPrimaryKey(SysUserAcl record);
}