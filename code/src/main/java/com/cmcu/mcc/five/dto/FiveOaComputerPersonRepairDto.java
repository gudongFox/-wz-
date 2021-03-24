package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerPersonRepair;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaComputerPersonRepairDto extends FiveOaComputerPersonRepair {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerPersonRepairDto adapt(FiveOaComputerPersonRepair item) {
        FiveOaComputerPersonRepairDto dto = new FiveOaComputerPersonRepairDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
