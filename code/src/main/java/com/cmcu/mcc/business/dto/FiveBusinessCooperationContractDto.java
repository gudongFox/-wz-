package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessCooperationContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBusinessCooperationContractDto extends FiveBusinessCooperationContract {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String attendUser;

    private String finishTime;


    public static FiveBusinessCooperationContractDto adapt(FiveBusinessCooperationContract item) {
        FiveBusinessCooperationContractDto dto=new FiveBusinessCooperationContractDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
