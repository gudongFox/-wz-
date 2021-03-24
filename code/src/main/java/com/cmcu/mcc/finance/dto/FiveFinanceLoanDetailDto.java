package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceLoanDetailDto extends FiveFinanceLoanDetail {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private String totalControlBalance;

    private String totalBudgetBalance;

    private String totalApplyMoney;

    public static FiveFinanceLoanDetailDto adapt(FiveFinanceLoanDetail item) {
        FiveFinanceLoanDetailDto dto = new FiveFinanceLoanDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
