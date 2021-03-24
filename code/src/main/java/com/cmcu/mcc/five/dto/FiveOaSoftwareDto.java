package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaSoftware;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSoftwareDto extends FiveOaSoftware {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaSoftwareDto adapt(FiveOaSoftware item) {
        FiveOaSoftwareDto dto = new FiveOaSoftwareDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

}
