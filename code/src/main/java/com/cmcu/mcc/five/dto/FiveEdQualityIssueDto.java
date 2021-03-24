package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdQualityIssue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdQualityIssueDto extends FiveEdQualityIssue {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdQualityIssueDto adapt(FiveEdQualityIssue item) {
        FiveEdQualityIssueDto dto = new FiveEdQualityIssueDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
