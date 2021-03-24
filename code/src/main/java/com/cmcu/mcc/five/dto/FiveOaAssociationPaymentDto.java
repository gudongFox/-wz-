package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaAssociationPayment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaAssociationPaymentDto extends FiveOaAssociationPayment {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaAssociationPaymentDto adapt(FiveOaAssociationPayment item) {
        FiveOaAssociationPaymentDto dto = new FiveOaAssociationPaymentDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
