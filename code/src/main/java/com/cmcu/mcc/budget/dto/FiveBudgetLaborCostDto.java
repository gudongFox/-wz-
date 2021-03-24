package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import com.cmcu.mcc.budget.entity.FiveBudgetLaborCost;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetLaborCostDto extends FiveBudgetLaborCost {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetLaborCostDto adapt(FiveBudgetLaborCost item) {
        FiveBudgetLaborCostDto dto = new FiveBudgetLaborCostDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
