package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.entity.FiveOaCardChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaCardChangeDto extends FiveOaCardChange {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static FiveOaCardChangeDto adapt(FiveOaCardChange item) {
        FiveOaCardChangeDto dto=new FiveOaCardChangeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
