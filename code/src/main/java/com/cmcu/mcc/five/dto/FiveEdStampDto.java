package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdServiceHandle;
import com.cmcu.mcc.five.entity.FiveEdStamp;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class FiveEdStampDto extends FiveEdStamp {

    private String processName;

   private String operateUserLogin;

    private String attendUser;


    public static FiveEdStampDto adapt(FiveEdStamp item) {
        FiveEdStampDto dto = new FiveEdStampDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}