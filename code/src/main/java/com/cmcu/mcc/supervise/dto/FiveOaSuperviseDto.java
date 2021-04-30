package com.cmcu.mcc.supervise.dto;

import com.cmcu.mcc.supervise.entity.FiveOaSupervise;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSuperviseDto extends FiveOaSupervise {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaSuperviseDto adapt(FiveOaSupervise item) {
        FiveOaSuperviseDto dto = new FiveOaSuperviseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
