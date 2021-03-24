package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaRulelawexamineDto extends FiveOaRulelawexamine {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaRulelawexamineDto adapt(FiveOaRulelawexamine item) {
        FiveOaRulelawexamineDto dto = new FiveOaRulelawexamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
