package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaNonSecretEquipmentScrap;
import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaNonSecretEquipmentScrapDto extends FiveOaNonSecretEquipmentScrap {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaNonSecretEquipmentScrapDto adapt(FiveOaNonSecretEquipmentScrap item) {
        FiveOaNonSecretEquipmentScrapDto dto = new FiveOaNonSecretEquipmentScrapDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
