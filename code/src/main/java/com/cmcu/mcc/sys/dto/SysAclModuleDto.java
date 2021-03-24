package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/26 17:13
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class SysAclModuleDto extends SysAclModule {

    public static SysAclModuleDto adapt(SysAclModule dept){
        SysAclModuleDto dto=new SysAclModuleDto();
        BeanUtils.copyProperties(dept,dto);
        dto.setParentName("根节点");
        return dto;
    }


    private String parentName;

}
