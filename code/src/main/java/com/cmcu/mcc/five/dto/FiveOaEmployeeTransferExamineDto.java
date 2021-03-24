package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEmployeeEntryExamine;
import com.cmcu.mcc.five.entity.FiveOaEmployeeTransferExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaEmployeeTransferExamineDto extends FiveOaEmployeeTransferExamine {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaEmployeeTransferExamineDto adapt(FiveOaEmployeeTransferExamine item) {
        FiveOaEmployeeTransferExamineDto dto = new FiveOaEmployeeTransferExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
