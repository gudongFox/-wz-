package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudget;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceProjectBudgetDto extends FiveFinanceProjectBudget {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceProjectBudgetDto adapt(FiveFinanceProjectBudget item) {
        FiveFinanceProjectBudgetDto dto=new FiveFinanceProjectBudgetDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
