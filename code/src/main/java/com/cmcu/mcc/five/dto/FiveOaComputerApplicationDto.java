package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerApplication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaComputerApplicationDto extends FiveOaComputerApplication {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerApplicationDto adapt(FiveOaComputerApplication item) {
        FiveOaComputerApplicationDto dto = new FiveOaComputerApplicationDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
