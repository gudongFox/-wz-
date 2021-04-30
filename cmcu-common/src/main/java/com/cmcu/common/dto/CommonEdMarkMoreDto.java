package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonEdMarkMore;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CommonEdMarkMoreDto extends CommonEdMarkMore {

    private String enLogin;

    private boolean editAble;

    public static CommonEdMarkMoreDto adapt(CommonEdMarkMore item) {
        CommonEdMarkMoreDto dto = new CommonEdMarkMoreDto();
        BeanUtils.copyProperties(item, dto);
        dto.setEditAble(false);
        return dto;
    }

}
