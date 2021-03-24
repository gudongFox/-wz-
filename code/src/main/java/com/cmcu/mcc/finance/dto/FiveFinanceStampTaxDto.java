package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceStampTaxDto extends FiveFinanceStampTax {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    public static FiveFinanceStampTaxDto adapt(FiveFinanceStampTax item) {
        FiveFinanceStampTaxDto dto=new FiveFinanceStampTaxDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
