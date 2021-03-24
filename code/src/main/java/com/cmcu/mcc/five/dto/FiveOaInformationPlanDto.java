package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationPlan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInformationPlanDto extends FiveOaInformationPlan {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaInformationPlanDto adapt(FiveOaInformationPlan item) {
        FiveOaInformationPlanDto dto = new FiveOaInformationPlanDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
