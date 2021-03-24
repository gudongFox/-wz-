package com.cmcu.mcc.ed.dto;


import com.cmcu.mcc.ed.entity.EdProject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class EdProjectDto extends EdProject {

    public static EdProjectDto adapt(EdProject item){
        EdProjectDto dto=new EdProjectDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

    private String deptName;

}
