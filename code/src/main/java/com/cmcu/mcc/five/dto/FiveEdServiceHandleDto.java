package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdServiceHandle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdServiceHandleDto extends FiveEdServiceHandle {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdServiceHandleDto adapt(FiveEdServiceHandle item) {
        FiveEdServiceHandleDto dto = new FiveEdServiceHandleDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
