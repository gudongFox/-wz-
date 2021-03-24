package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessCustomer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessCustomerDto extends BusinessCustomer {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private long contractCount;

    public static BusinessCustomerDto adapt(BusinessCustomer item){
        BusinessCustomerDto dto=new BusinessCustomerDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
