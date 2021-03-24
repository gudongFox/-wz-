package com.cmcu.mcc.sys.dto;


import com.cmcu.mcc.sys.entity.SysAcl;
import com.cmcu.mcc.sys.entity.SysRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class SysRoleDto extends SysRole {

    private String childDeptNames;

    private String icon;

    private int parentId;

    private String userLogin;

    public static SysRoleDto adapt(SysRole item){
        SysRoleDto dto=new SysRoleDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
