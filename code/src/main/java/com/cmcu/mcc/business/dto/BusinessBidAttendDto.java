package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessBidAttend;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessBidAttendDto extends BusinessBidAttend {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static BusinessBidAttendDto adapt(BusinessBidAttend item) {
        BusinessBidAttendDto dto=new BusinessBidAttendDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
