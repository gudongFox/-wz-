package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import com.cmcu.mcc.finance.entity.FiveFinanceReceipt;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceReceiptDto extends FiveFinanceReceipt {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveFinanceReceiptDto adapt(FiveFinanceReceipt item) {
        FiveFinanceReceiptDto dto = new FiveFinanceReceiptDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
