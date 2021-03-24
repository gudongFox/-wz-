package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaDispatchCpcaAcademy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaDispatchCpcaAcademyDto extends FiveOaDispatchCpcaAcademy {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaDispatchCpcaAcademyDto adapt(FiveOaDispatchCpcaAcademy item) {
        FiveOaDispatchCpcaAcademyDto dto = new FiveOaDispatchCpcaAcademyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
