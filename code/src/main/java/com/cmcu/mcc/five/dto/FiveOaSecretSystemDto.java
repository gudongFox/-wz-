package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaSecretSystem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSecretSystemDto extends FiveOaSecretSystem {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaSecretSystemDto adapt(FiveOaSecretSystem item) {
        FiveOaSecretSystemDto dto = new FiveOaSecretSystemDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
