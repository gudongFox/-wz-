package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaBidApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaBidApplyDto extends FiveOaBidApply {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaBidApplyDto adapt(FiveOaBidApply item) {
        FiveOaBidApplyDto dto = new FiveOaBidApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
