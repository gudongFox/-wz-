package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccounts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceTransferAccountsDto extends FiveFinanceTransferAccounts {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private String totalMoney;
    //是否存在项目
    private Boolean isProject;

    public static FiveFinanceTransferAccountsDto adapt(FiveFinanceTransferAccounts item) {
        FiveFinanceTransferAccountsDto dto = new FiveFinanceTransferAccountsDto();
        BeanUtils.copyProperties(item, dto);


        return dto;
    }
    FiveFinanceTransferAccountsDto(){
        isProject = false;
    }
}
