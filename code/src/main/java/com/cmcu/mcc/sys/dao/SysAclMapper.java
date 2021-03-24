package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.dto.SysAclDto;
import com.cmcu.mcc.sys.entity.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    List<SysAclDto> selectAll(Map params);

    List<SysAcl> listMenuByRoleIdList(@Param("roleIdList") Integer[] roleIdList);

    int updateByPrimaryKey(SysAcl record);
}