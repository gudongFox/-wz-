package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.entity.OaHardware;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaHardwareDto extends OaHardware {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaHardwareDto adapt(OaHardware item) {
        OaHardwareDto dto=new OaHardwareDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
