package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.entity.BusinessContractDetail;
import com.cmcu.mcc.business.entity.BusinessPreContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessPreContractDto extends BusinessPreContract {


    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;


    public static BusinessPreContractDto adapt(BusinessPreContract item) {
        BusinessPreContractDto dto=new BusinessPreContractDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
