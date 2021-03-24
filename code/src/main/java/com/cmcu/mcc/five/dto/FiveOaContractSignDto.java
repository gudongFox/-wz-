package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaContractSign;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaContractSignDto extends FiveOaContractSign {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaContractSignDto adapt(FiveOaContractSign item) {
        FiveOaContractSignDto dto = new FiveOaContractSignDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
