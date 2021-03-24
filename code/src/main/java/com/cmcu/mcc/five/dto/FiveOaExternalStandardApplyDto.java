package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaExternalStandardApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveOaExternalStandardApplyDto extends FiveOaExternalStandardApply {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaExternalStandardApplyDto adapt (FiveOaExternalStandardApply item){
        FiveOaExternalStandardApplyDto dto=new FiveOaExternalStandardApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
