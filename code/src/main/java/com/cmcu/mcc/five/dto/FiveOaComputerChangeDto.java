package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaComputerChangeDto extends FiveOaComputerChange {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerChangeDto adapt(FiveOaComputerChange item) {
        FiveOaComputerChangeDto dto = new FiveOaComputerChangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
