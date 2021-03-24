package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceOutSupply;
import com.cmcu.mcc.finance.entity.FiveFinanceSelfBank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceSelfBankDto extends FiveFinanceSelfBank {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    public static FiveFinanceSelfBankDto adapt(FiveFinanceSelfBank item) {
        FiveFinanceSelfBankDto dto=new FiveFinanceSelfBankDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
