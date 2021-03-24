package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceInvoiceDto extends FiveFinanceInvoice {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    private String preContractMoney;

    private String shouldContractMoney;

    public static FiveFinanceInvoiceDto adapt(FiveFinanceInvoice item) {
        FiveFinanceInvoiceDto dto=new FiveFinanceInvoiceDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
