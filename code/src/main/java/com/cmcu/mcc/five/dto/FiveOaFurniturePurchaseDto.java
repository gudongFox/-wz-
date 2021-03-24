package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaFurniturePurchase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaFurniturePurchaseDto extends FiveOaFurniturePurchase {
    private String processName;

    private String operateUserLogin;

    public static FiveOaFurniturePurchaseDto adapt(FiveOaFurniturePurchase item) {
        FiveOaFurniturePurchaseDto dto = new FiveOaFurniturePurchaseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
