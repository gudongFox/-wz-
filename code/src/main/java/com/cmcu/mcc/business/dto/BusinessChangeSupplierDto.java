package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessChangeSupplier;
import com.cmcu.mcc.business.entity.BusinessSupplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessChangeSupplierDto extends BusinessChangeSupplier {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String attendUser;

    private String finishTime;

    public static BusinessChangeSupplierDto adapt(BusinessChangeSupplier item) {
        BusinessChangeSupplierDto dto=new BusinessChangeSupplierDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
