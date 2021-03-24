package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessSupplier;
import com.cmcu.mcc.business.entity.BusinessSupplierAptitude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessAptitudeDto extends BusinessSupplierAptitude {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static BusinessAptitudeDto adapt(BusinessSupplierAptitude item) {
        BusinessAptitudeDto dto=new BusinessAptitudeDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
