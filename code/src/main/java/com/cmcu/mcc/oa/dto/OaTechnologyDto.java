package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaTechnology;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class OaTechnologyDto extends OaTechnology {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaTechnologyDto adapt(OaTechnology item) {
        OaTechnologyDto dto=new OaTechnologyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
