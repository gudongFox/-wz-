package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCancel;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCollection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceInvoiceCollectionDto extends FiveFinanceInvoiceCollection {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;



    public static FiveFinanceInvoiceCollectionDto adapt(FiveFinanceInvoiceCollection item) {
        FiveFinanceInvoiceCollectionDto dto=new FiveFinanceInvoiceCollectionDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
