package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrContract;
import com.cmcu.mcc.hr.entity.HrEmployee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrContractDto extends HrContract {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    private String userName;

    public static HrContractDto adapt(HrContract item){
        HrContractDto dto=new HrContractDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
