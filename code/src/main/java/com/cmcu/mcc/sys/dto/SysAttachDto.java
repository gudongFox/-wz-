package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysAttach;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SysAttachDto extends SysAttach {

    public static SysAttachDto adapt(SysAttach item){
        SysAttachDto dto=new SysAttachDto();
        if(item!=null)  BeanUtils.copyProperties(item,dto);
        return dto;
    }

    public String creatorName;

    private String sizeName;

    private String extensionName;

}
