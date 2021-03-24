package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import com.cmcu.mcc.budget.entity.FiveBudgetFee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetFeeDto extends FiveBudgetFee {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetFeeDto adapt(FiveBudgetFee item) {
        FiveBudgetFeeDto dto = new FiveBudgetFeeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
