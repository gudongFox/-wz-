package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerSafe;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaComputerSafeDto extends FiveOaComputerSafe {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerSafeDto adapt(FiveOaComputerSafe item) {
        FiveOaComputerSafeDto dto = new FiveOaComputerSafeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
