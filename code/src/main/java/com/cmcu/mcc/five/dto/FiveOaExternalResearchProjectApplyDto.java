package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveOaExternalResearchProjectApplyDto extends FiveOaExternalResearchProjectApply {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaExternalResearchProjectApplyDto adapt(FiveOaExternalResearchProjectApply item) {
        FiveOaExternalResearchProjectApplyDto dto = new FiveOaExternalResearchProjectApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
