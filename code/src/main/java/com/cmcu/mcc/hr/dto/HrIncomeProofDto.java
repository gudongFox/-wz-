package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrIncomeProof;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrIncomeProofDto extends HrIncomeProof {

    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrIncomeProofDto adapt(HrIncomeProof item){
        HrIncomeProofDto dto=new HrIncomeProofDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
