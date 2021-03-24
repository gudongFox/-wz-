package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceCompanyBank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceCompanyBankDto extends FiveFinanceCompanyBank {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    public static FiveFinanceCompanyBankDto adapt(FiveFinanceCompanyBank item) {
        FiveFinanceCompanyBankDto dto=new FiveFinanceCompanyBankDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
