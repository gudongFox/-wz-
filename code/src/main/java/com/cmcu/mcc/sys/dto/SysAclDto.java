package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysAcl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class SysAclDto extends SysAcl {

    public static SysAclDto adapt(SysAcl item){
        SysAclDto dto=new SysAclDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


    private List<String> optList;

    private String aclModuleName;

}
