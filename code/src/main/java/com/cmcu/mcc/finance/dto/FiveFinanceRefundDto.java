package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceRefund;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceRefundDto extends FiveFinanceRefund {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveFinanceRefundDto adapt(FiveFinanceRefund item) {
        FiveFinanceRefundDto dto = new FiveFinanceRefundDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
