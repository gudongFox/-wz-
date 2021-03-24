package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceInvoiceDetailDto extends FiveFinanceInvoiceDetail {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceInvoiceDetailDto adapt(FiveFinanceInvoiceDetail item) {
        FiveFinanceInvoiceDetailDto dto=new FiveFinanceInvoiceDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
