package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaEmployeeRetireExamine;
import com.cmcu.mcc.five.entity.FiveOaProcessDevelopApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaProcessDevelopApplyDto extends FiveOaProcessDevelopApply {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaProcessDevelopApplyDto adapt(FiveOaProcessDevelopApply item) {
        FiveOaProcessDevelopApplyDto dto = new FiveOaProcessDevelopApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
