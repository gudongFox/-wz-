package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDispatchAcademy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Setter
@Getter
public class FiveOaDispatchAcademyDto extends FiveOaDispatchAcademy {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaDispatchAcademyDto adapt(FiveOaDispatchAcademy item) {
        FiveOaDispatchAcademyDto dto = new FiveOaDispatchAcademyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
