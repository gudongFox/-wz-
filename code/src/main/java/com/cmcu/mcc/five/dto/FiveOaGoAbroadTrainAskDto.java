package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAsk;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaGoAbroadTrainAskDto extends FiveOaGoAbroadTrainAsk {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaGoAbroadTrainAskDto adapt(FiveOaGoAbroadTrainAsk item) {
        FiveOaGoAbroadTrainAskDto dto = new FiveOaGoAbroadTrainAskDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
