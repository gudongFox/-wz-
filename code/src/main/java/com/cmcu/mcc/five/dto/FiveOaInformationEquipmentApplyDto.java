package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaInformationEquipmentApplyDto extends FiveOaInformationEquipmentApply {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaInformationEquipmentApplyDto adapt(FiveOaInformationEquipmentApply item) {
        FiveOaInformationEquipmentApplyDto dto = new FiveOaInformationEquipmentApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
