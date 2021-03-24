package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependent;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsOut;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetScientificFundsOutDto extends FiveBudgetScientificFundsOut {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetScientificFundsOutDto adapt(FiveBudgetScientificFundsOut item) {
        FiveBudgetScientificFundsOutDto dto = new FiveBudgetScientificFundsOutDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
