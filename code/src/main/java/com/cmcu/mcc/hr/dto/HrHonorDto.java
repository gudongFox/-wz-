package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrHonor;
import com.cmcu.mcc.hr.entity.HrInsure;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrHonorDto extends HrHonor {

    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrHonorDto adapt(HrHonor item){
        HrHonorDto dto=new HrHonorDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
