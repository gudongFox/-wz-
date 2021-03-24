package com.cmcu.mcc.sys.dto;


import com.cmcu.mcc.sys.entity.SysAcl;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SysRoleAclDto extends SysAcl {

    private Integer roleId;

    private Integer aclId;

    private String roleName;

    private String selectOpts;


}
