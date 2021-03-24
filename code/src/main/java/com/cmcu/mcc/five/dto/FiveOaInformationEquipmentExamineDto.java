package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInformationEquipmentExamineDto extends FiveOaInformationEquipmentExamine {
    private String processName;

    private String operateUserLogin;

    private FiveOaInformationEquipmentApply equipmentApply;

    public static FiveOaInformationEquipmentExamineDto adapt(FiveOaInformationEquipmentExamine item) {
        FiveOaInformationEquipmentExamineDto dto = new FiveOaInformationEquipmentExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
