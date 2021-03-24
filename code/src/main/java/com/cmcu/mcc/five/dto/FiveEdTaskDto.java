package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdArrange;
import com.cmcu.mcc.five.entity.FiveEdTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdTaskDto extends FiveEdTask {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdTaskDto adapt(FiveEdTask item) {
        FiveEdTaskDto dto = new FiveEdTaskDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
