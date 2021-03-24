package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEmployeeEntryExamine;
import com.cmcu.mcc.five.entity.FiveOaGoAbroad;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaEmployeeEntryExamineDto extends FiveOaEmployeeEntryExamine {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaEmployeeEntryExamineDto adapt(FiveOaEmployeeEntryExamine item) {
        FiveOaEmployeeEntryExamineDto dto = new FiveOaEmployeeEntryExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
