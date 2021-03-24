package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessAdvance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessAdvanceDto extends FiveBusinessAdvance {
    private String processName;

    private String operateUserLogin;

    public static FiveBusinessAdvanceDto adapt(FiveBusinessAdvance item) {
        FiveBusinessAdvanceDto dto = new FiveBusinessAdvanceDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
