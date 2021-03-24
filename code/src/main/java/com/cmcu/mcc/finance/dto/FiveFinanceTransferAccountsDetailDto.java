package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccounts;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceTransferAccountsDetailDto extends FiveFinanceTransferAccountsDetail {

    private String operateUserLogin;

    private String finishTime;


    public static FiveFinanceTransferAccountsDetailDto adapt(FiveFinanceTransferAccountsDetail item) {
        FiveFinanceTransferAccountsDetailDto dto = new FiveFinanceTransferAccountsDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
