package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetIndependentDetail;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintain;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintainDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetMaintainDto extends FiveBudgetMaintain {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;
    
    public static FiveBudgetMaintainDto adapt(FiveBudgetMaintain item) {
        FiveBudgetMaintainDto dto = new FiveBudgetMaintainDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
