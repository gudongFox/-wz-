package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaOutSpecialist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaOutSpecialistDto extends FiveOaOutSpecialist {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaOutSpecialistDto adapt(FiveOaOutSpecialist item) {
        FiveOaOutSpecialistDto dto = new FiveOaOutSpecialistDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}

