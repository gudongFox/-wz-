package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceLoanDto extends FiveFinanceLoan {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private String totalControlBalance;

    private String totalBudgetBalance;

    private String totalApplyMoney;

    //借款剩余金额
    private String remainMoney;
    //是否存在项目
    private Boolean isProject;

    public static FiveFinanceLoanDto adapt(FiveFinanceLoan item) {
        FiveFinanceLoanDto dto = new FiveFinanceLoanDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
    FiveFinanceLoanDto(){
        isProject = false;
    }
}
