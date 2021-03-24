package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceIncomeDto extends FiveFinanceIncome {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    //是否认领完
    private Boolean isFullMoney;

    private String moneyNode;

    private String moneyNodeMax;

    //收入认领人
    private String confirmCreatorName;

    //收入确认 是否确认
    private Boolean isConfirmed;


    public static FiveFinanceIncomeDto adapt(FiveFinanceIncome item) {
        FiveFinanceIncomeDto dto=new FiveFinanceIncomeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
