package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaReport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaReportDto extends FiveOaReport {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaReportDto adapt(FiveOaReport item) {
        FiveOaReportDto dto = new FiveOaReportDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
