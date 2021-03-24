package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetIndependentDto extends FiveBudgetIndependent {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;


    public static FiveBudgetIndependentDto adapt(FiveBudgetIndependent item) {
        FiveBudgetIndependentDto dto = new FiveBudgetIndependentDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
