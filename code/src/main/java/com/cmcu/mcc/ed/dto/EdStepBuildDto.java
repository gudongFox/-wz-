package com.cmcu.mcc.ed.dto;

import com.cmcu.mcc.ed.entity.EdStepBuild;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class EdStepBuildDto extends EdStepBuild {

    private String sizeName;

    private long size;

    public static EdStepBuildDto adapt(EdStepBuild item){
        EdStepBuildDto dto=new EdStepBuildDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
