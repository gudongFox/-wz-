package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerPurchase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaComputerPurchaseDto extends FiveOaComputerPurchase {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerPurchaseDto adapt(FiveOaComputerPurchase item) {
        FiveOaComputerPurchaseDto dto = new FiveOaComputerPurchaseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
