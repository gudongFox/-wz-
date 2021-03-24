package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import com.cmcu.mcc.five.entity.FiveOaScientificTaskCostApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaScientificTaskCostApplyDto extends FiveOaScientificTaskCostApply {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaScientificTaskCostApplyDto adapt(FiveOaScientificTaskCostApply item) {
        FiveOaScientificTaskCostApplyDto dto = new FiveOaScientificTaskCostApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
