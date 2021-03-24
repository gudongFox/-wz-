package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.dto.SysRoleUserDto;
import com.cmcu.mcc.sys.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRoleUserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    List<SysRoleUser> selectAll();

    int updateByPrimaryKey(SysRoleUser record);

    List<SysRoleUserDto> listExtendByUserId(int userId);

    List<SysRoleUserDto> listExtendByRoleId(@Param("q") String q, @Param("roleId") int roleId);

    void deleteByRoleId(int roleId);

    List<Map> listUserByRoleName(String roleName);

}