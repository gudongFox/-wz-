package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpense;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceTravelExpenseDto extends FiveFinanceTravelExpense {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private String totalApplyMoney;

    private String totalOnRoadSubsidy;

    private String countTotal;

    private String deductionMoney;
    //是否存在项目
    private Boolean isProject;
    //借款剩余金额
    private String loanRemainMoney;

    public static FiveFinanceTravelExpenseDto adapt(FiveFinanceTravelExpense item) {
        FiveFinanceTravelExpenseDto dto = new FiveFinanceTravelExpenseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
    FiveFinanceTravelExpenseDto(){
        isProject = false;
    }
}
