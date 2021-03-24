package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.dto.SysRoleAclDto;
import com.cmcu.mcc.sys.entity.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysRoleAcl record);

    List<SysRoleAclDto> selectExtendAll(Map params);

    List<SysRoleAcl> selectAllByRoleId(int roleId);

    void deleteByRoleId(int roleId);

    List<Map> listMenuByRoleIdList(@Param("roleIdList") Integer[] roleIdList);

    List<String> listAclByRoleIdList(@Param("roleIdList") Integer[] roleIdList);
}