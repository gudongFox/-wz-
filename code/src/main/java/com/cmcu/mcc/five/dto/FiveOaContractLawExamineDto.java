package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaContractLawExamine;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaContractLawExamineDto extends FiveOaContractLawExamine {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaContractLawExamineDto adapt(FiveOaContractLawExamine item) {
        FiveOaContractLawExamineDto dto = new FiveOaContractLawExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
