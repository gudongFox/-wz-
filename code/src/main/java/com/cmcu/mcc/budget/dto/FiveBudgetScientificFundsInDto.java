package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsIn;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetScientificFundsInDto extends FiveBudgetScientificFundsIn {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    //去年
    private String lastYear;

    public static FiveBudgetScientificFundsInDto adapt(FiveBudgetScientificFundsIn item) {
        FiveBudgetScientificFundsInDto dto = new FiveBudgetScientificFundsInDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
