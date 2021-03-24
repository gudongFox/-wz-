package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessChangeCustomer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessChangeCustomerDto extends BusinessChangeCustomer {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private long contractCount;

    public static BusinessChangeCustomerDto adapt(BusinessChangeCustomer item){
        BusinessChangeCustomerDto dto=new BusinessChangeCustomerDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
