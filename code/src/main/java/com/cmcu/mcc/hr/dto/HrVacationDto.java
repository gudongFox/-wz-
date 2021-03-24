package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrVacation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrVacationDto extends HrVacation {

    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrVacationDto adapt(HrVacation item){
        HrVacationDto dto=new HrVacationDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}