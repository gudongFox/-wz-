package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/25 11:44
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class SysUserDto extends SysUser {

    private String deptName;


    private String roleName;


    public static SysUserDto adapt(SysUser sysUser){
        SysUserDto dto=new SysUserDto();
        BeanUtils.copyProperties(sysUser,dto);
        return dto;
    }
}
