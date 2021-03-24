package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessInputContract;
import com.cmcu.mcc.business.entity.BusinessPreContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class BusinessInputContractDto extends BusinessInputContract {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;


    public static BusinessInputContractDto adapt(BusinessInputContract item) {
        BusinessInputContractDto dto=new BusinessInputContractDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
