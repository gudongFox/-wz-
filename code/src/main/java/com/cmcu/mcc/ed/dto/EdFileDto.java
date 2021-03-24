package com.cmcu.mcc.ed.dto;

import com.cmcu.mcc.ed.entity.EdFile;
import com.cmcu.mcc.sys.entity.SysAttach;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class EdFileDto extends EdFile {


    public static EdFileDto adapt(EdFile item){
        EdFileDto dto=new EdFileDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

    private SysAttach sysAttach;

    private String sizeName;

    private String extensionName;

    private String source;

    private String md5;

    private boolean selected;

    private String signMd5;


}
