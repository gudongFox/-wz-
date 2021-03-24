package com.cmcu.mcc.ed.dto;

import com.cmcu.mcc.ed.entity.EdProjectTree;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class EdProjectTreeDto extends EdProjectTree {

    public static EdProjectTreeDto adapt(EdProjectTree item){
        EdProjectTreeDto dto=new EdProjectTreeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

    private String parentNodeName;

    private int parentForeignKey;

}
