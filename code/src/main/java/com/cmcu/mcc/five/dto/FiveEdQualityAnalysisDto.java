package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdQualityAnalysis;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author hnz
 * @date 2019/12/2
 */
@Setter
@Getter
public class FiveEdQualityAnalysisDto extends FiveEdQualityAnalysis {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String businessKey;

    private String finishTime;

    public static FiveEdQualityAnalysisDto adapt(FiveEdQualityAnalysis item) {
        FiveEdQualityAnalysisDto dto = new FiveEdQualityAnalysisDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
