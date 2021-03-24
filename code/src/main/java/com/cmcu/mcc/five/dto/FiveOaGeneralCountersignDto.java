package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaGeneralCountersign;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;



@Getter
@Setter
public class FiveOaGeneralCountersignDto extends FiveOaGeneralCountersign {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaGeneralCountersignDto adapt(FiveOaGeneralCountersign item) {
        FiveOaGeneralCountersignDto dto = new FiveOaGeneralCountersignDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }



}
