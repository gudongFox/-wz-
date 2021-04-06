package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetFeeChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetFeeChangeDto extends FiveBudgetFeeChange {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //去年
    private String lastYear;

    public static FiveBudgetFeeChangeDto adapt(FiveBudgetFeeChange item) {
        FiveBudgetFeeChangeDto dto = new FiveBudgetFeeChangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
