package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEmployeeDimissionExamine;
import com.cmcu.mcc.five.entity.FiveOaEmployeeRetireExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaEmployeeRetireExamineDto extends FiveOaEmployeeRetireExamine {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaEmployeeRetireExamineDto adapt(FiveOaEmployeeRetireExamine item) {
        FiveOaEmployeeRetireExamineDto dto = new FiveOaEmployeeRetireExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
