package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudget;
import com.cmcu.mcc.finance.entity.FiveFinanceDeptFund;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveFinanceDeptFundDto extends FiveFinanceDeptFund {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String nowMoney;

    private String subMoneyTotal;

    //事业部预算
    private String deptBudgetMoney;
    private int deptBudgetId;

    //使用信息
    private List<FiveFinanceDeptFundDetailDto> details;


    public static FiveFinanceDeptFundDto adapt(FiveFinanceDeptFund item) {
        FiveFinanceDeptFundDto dto=new FiveFinanceDeptFundDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
