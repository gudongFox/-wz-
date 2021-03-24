package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaNewsexamine;
import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaNewsexamineDto extends FiveOaNewsexamine {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaNewsexamineDto adapt(FiveOaNewsexamine item) {
        FiveOaNewsexamineDto dto = new FiveOaNewsexamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
