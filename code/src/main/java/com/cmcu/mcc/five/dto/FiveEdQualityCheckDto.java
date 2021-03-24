package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdQualityCheck;
import com.cmcu.mcc.five.entity.FiveEdStamp;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdQualityCheckDto  extends FiveEdQualityCheck {

    private String processName;

   private String operateUserLogin;

    private String attendUser;


    public static FiveEdQualityCheckDto adapt(FiveEdQualityCheck item) {
        FiveEdQualityCheckDto dto = new FiveEdQualityCheckDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
