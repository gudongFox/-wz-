package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInstitutionCountSign;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInstitutionCountSignDto extends FiveOaInstitutionCountSign {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaInstitutionCountSignDto adapt(FiveOaInstitutionCountSign item) {
        FiveOaInstitutionCountSignDto dto = new FiveOaInstitutionCountSignDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
