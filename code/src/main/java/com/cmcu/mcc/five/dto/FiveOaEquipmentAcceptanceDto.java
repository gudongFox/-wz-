package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveOaEquipmentAcceptanceDto extends FiveOaEquipmentAcceptance {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaEquipmentAcceptanceDto adapt(FiveOaEquipmentAcceptance item) {
        FiveOaEquipmentAcceptanceDto dto = new FiveOaEquipmentAcceptanceDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
