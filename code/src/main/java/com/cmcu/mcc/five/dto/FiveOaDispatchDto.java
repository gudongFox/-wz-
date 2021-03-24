package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDispatch;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDispatchDto extends FiveOaDispatch {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;


    public static FiveOaDispatchDto adapt(FiveOaDispatch item) {
        FiveOaDispatchDto dto = new FiveOaDispatchDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
