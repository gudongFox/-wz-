package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaOutTechnicalExchange;
import com.cmcu.mcc.five.entity.FiveOaProfessionalSkillTrain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaOutTechnicalExchangeDto extends FiveOaOutTechnicalExchange {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaOutTechnicalExchangeDto adapt(FiveOaOutTechnicalExchange item) {
        FiveOaOutTechnicalExchangeDto dto = new FiveOaOutTechnicalExchangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
