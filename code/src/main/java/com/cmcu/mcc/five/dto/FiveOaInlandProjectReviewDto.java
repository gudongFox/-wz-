package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInlandProjectApplyDto extends FiveOaInlandProjectApply {

    private String processName;

    private String operateUserLogin;

    public static FiveOaInlandProjectApplyDto adapt(FiveOaInlandProjectApply item) {
        FiveOaInlandProjectApplyDto dto = new FiveOaInlandProjectApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
