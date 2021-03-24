package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDispatchCpc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDispatchCpcDto extends FiveOaDispatchCpc {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    public static FiveOaDispatchCpcDto adapt(FiveOaDispatchCpc item) {
        FiveOaDispatchCpcDto dto = new FiveOaDispatchCpcDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
