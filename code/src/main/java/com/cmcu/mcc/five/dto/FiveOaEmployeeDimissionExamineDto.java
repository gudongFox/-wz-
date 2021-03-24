package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEmployeeDimissionExamine;
import com.cmcu.mcc.five.entity.FiveOaEmployeeTransferExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaEmployeeDimissionExamineDto extends FiveOaEmployeeDimissionExamine {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaEmployeeDimissionExamineDto adapt(FiveOaEmployeeDimissionExamine item) {
        FiveOaEmployeeDimissionExamineDto dto = new FiveOaEmployeeDimissionExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
