package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaHardware;
import com.cmcu.mcc.oa.entity.OaSoftware;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaSoftwareDto extends OaSoftware {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaSoftwareDto adapt(OaSoftware item) {
        OaSoftwareDto dto=new OaSoftwareDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
