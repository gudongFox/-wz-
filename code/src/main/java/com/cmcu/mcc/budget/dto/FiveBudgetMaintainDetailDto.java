package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependentDetail;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintainDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetMaintainDetailDto extends FiveBudgetMaintainDetail {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //预算剩余金额
    private String remainMoney;


    public static FiveBudgetMaintainDetailDto adapt(FiveBudgetMaintainDetail item) {
        FiveBudgetMaintainDetailDto dto = new FiveBudgetMaintainDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
