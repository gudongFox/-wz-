package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrEducation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrEducationDto extends HrEducation {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    private String userName;

    public static HrEducationDto adapt(HrEducation item){
        HrEducationDto dto=new HrEducationDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
