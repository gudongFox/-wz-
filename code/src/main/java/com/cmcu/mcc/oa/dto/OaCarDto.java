package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaCar;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaCarDto extends OaCar {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaCarDto adapt(OaCar item) {
        OaCarDto dto=new OaCarDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
