package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaMaterialPurchase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaMaterialPurchaseDto extends FiveOaMaterialPurchase {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private double contNums;

    public static FiveOaMaterialPurchaseDto adapt(FiveOaMaterialPurchase item) {
        FiveOaMaterialPurchaseDto dto = new FiveOaMaterialPurchaseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
