package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudget;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceDeptBudgetDto extends FiveFinanceDeptBudget {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;
    //是否认领完
    private Boolean isFullMoney;

    public static FiveFinanceDeptBudgetDto adapt(FiveFinanceDeptBudget item) {
        FiveFinanceDeptBudgetDto dto=new FiveFinanceDeptBudgetDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
