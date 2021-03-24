package com.cmcu.mcc.sys.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysRoleAcl {
    private Integer id;

    private Integer roleId;

    private Integer aclId;

    private String selectOpts;
}