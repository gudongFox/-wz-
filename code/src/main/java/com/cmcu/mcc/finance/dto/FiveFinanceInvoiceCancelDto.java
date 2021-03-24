package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCancel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceInvoiceCancelDto extends FiveFinanceInvoiceCancel {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceInvoiceCancelDto adapt(FiveFinanceInvoiceCancel item) {
        FiveFinanceInvoiceCancelDto dto=new FiveFinanceInvoiceCancelDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
