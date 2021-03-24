package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerMaintain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaComputerMaintainDto extends FiveOaComputerMaintain {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerMaintainDto adapt(FiveOaComputerMaintain item) {
        FiveOaComputerMaintainDto dto = new FiveOaComputerMaintainDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
