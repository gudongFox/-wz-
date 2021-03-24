package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessBidContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessBidContractDto extends BusinessBidContract {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static BusinessBidContractDto adapt(BusinessBidContract item) {
        BusinessBidContractDto dto=new BusinessBidContractDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
