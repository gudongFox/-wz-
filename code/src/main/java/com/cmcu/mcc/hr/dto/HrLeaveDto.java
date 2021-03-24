package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrIncomeProof;
import com.cmcu.mcc.hr.entity.HrLeave;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrLeaveDto extends HrLeave {

    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrLeaveDto adapt(HrLeave item){
        HrLeaveDto dto=new HrLeaveDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}