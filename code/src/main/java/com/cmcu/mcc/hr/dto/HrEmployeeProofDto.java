package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrEmployeeProof;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Setter
@Getter
public class HrEmployeeProofDto extends HrEmployeeProof {
    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrEmployeeProofDto adapt(HrEmployeeProof item){
        HrEmployeeProofDto dto=new HrEmployeeProofDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
