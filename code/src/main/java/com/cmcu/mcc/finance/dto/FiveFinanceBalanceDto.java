package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceBalance;
import com.cmcu.mcc.finance.entity.FiveFinanceNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceBalanceDto extends FiveFinanceBalance {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceBalanceDto adapt(FiveFinanceBalance item) {
        FiveFinanceBalanceDto dto=new FiveFinanceBalanceDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
