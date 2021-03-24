package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/28 8:40
 * To change this template use File | Settings | File Templates.
 */


@Getter
@Setter
public class SysRoleUserDto extends SysUser {

    private String roleName;

    private String roleNames;

    private String deptName;

    private Integer roleId;

    private Integer userId;

    private Boolean mine;

}
