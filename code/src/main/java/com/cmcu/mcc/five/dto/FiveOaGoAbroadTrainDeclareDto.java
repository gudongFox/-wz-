package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainDeclare;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaGoAbroadTrainDeclareDto extends FiveOaGoAbroadTrainDeclare {
    private String processName;

  private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaGoAbroadTrainDeclareDto adapt(FiveOaGoAbroadTrainDeclare item) {
        FiveOaGoAbroadTrainDeclareDto dto = new FiveOaGoAbroadTrainDeclareDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
