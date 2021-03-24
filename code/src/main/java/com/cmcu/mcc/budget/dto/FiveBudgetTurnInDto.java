package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import com.cmcu.mcc.budget.entity.FiveBudgetTurnIn;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetTurnInDto extends FiveBudgetTurnIn {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetTurnInDto adapt(FiveBudgetTurnIn item) {
        FiveBudgetTurnInDto dto = new FiveBudgetTurnInDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
