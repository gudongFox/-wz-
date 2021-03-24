package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessBidApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessBidApplyDto extends BusinessBidApply{

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static BusinessBidApplyDto adapt(BusinessBidApply item) {
        BusinessBidApplyDto dto=new BusinessBidApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
