package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrInvent;
import com.cmcu.mcc.hr.entity.HrLeave;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrInventDto extends HrInvent {

    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrInventDto adapt(HrInvent item){
        HrInventDto dto=new HrInventDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}