package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessIncome;
import com.cmcu.mcc.business.entity.BusinessRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessIncomeDto extends BusinessIncome {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    //发票已收金额
    private String invoiceGetMoney;
    //发票开具金额
    private String invoiceMoney;


    public static BusinessIncomeDto adapt(BusinessIncome item) {
        BusinessIncomeDto dto=new BusinessIncomeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
