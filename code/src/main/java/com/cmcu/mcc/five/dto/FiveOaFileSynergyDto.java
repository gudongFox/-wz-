package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaFileSynergy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaFileSynergyDto extends FiveOaFileSynergy {
    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaFileSynergyDto adapt(FiveOaFileSynergy item) {
        FiveOaFileSynergyDto dto = new FiveOaFileSynergyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
