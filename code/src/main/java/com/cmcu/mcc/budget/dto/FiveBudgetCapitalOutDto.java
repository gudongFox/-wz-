package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import com.cmcu.mcc.budget.entity.FiveBudgetPublicFunds;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetCapitalOutDto extends FiveBudgetCapitalOut {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetCapitalOutDto adapt(FiveBudgetCapitalOut item) {
        FiveBudgetCapitalOutDto dto = new FiveBudgetCapitalOutDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
