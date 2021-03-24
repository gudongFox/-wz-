package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaInformationEquipmentProcurementDto extends FiveOaInformationEquipmentProcurement {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaInformationEquipmentProcurementDto adapt(FiveOaInformationEquipmentProcurement item) {
        FiveOaInformationEquipmentProcurementDto dto = new FiveOaInformationEquipmentProcurementDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
