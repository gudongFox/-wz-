package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaPlatformRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaPlatformRecordDto extends FiveOaPlatformRecord {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaPlatformRecordDto adapt(FiveOaPlatformRecord item) {
        FiveOaPlatformRecordDto dto = new FiveOaPlatformRecordDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
