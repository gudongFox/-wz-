package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessStatistics;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessStatisticsDto extends FiveBusinessStatistics {
    private String processName;

    private String operateUserLogin;

    public static FiveBusinessStatisticsDto adapt(FiveBusinessStatistics item) {
        FiveBusinessStatisticsDto dto = new FiveBusinessStatisticsDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
