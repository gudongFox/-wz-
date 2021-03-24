package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessCooperationDelegation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBusinessCooperationDelegationDto extends FiveBusinessCooperationDelegation {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String attendUser;

    private String finishTime;


    public static FiveBusinessCooperationDelegationDto adapt(FiveBusinessCooperationDelegation item) {
        FiveBusinessCooperationDelegationDto dto=new FiveBusinessCooperationDelegationDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
