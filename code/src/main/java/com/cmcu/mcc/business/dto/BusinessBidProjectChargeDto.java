package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessBidProjectCharge;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessBidProjectChargeDto extends BusinessBidProjectCharge {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static BusinessBidProjectChargeDto adapt(BusinessBidProjectCharge item) {
        BusinessBidProjectChargeDto dto=new BusinessBidProjectChargeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
