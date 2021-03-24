package com.cmcu.mcc.supervise.dto;

import com.cmcu.mcc.supervise.entity.FiveOaSupervise;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSuperviseDetailDto extends FiveOaSuperviseDetail {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaSuperviseDetailDto adapt(FiveOaSuperviseDetail item) {
        FiveOaSuperviseDetailDto dto = new FiveOaSuperviseDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
