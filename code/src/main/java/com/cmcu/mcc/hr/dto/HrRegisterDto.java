package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrRegister;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrRegisterDto extends HrRegister {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    private String userName;

    public static HrRegisterDto adapt(HrRegister item){
        HrRegisterDto dto=new HrRegisterDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
