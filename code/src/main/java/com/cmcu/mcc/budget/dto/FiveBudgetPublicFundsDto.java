package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetPublicFunds;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetPublicFundsDto extends FiveBudgetPublicFunds {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;


    public static FiveBudgetPublicFundsDto adapt(FiveBudgetPublicFunds item) {
        FiveBudgetPublicFundsDto dto = new FiveBudgetPublicFundsDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
