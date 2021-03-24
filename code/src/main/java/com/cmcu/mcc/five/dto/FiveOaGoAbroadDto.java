package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaGoAbroad;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaGoAbroadDto extends FiveOaGoAbroad {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaGoAbroadDto adapt(FiveOaGoAbroad item) {
        FiveOaGoAbroadDto dto = new FiveOaGoAbroadDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
