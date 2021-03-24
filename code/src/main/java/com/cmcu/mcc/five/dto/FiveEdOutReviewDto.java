package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdOutReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdOutReviewDto extends FiveEdOutReview {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String businessKey;

    private String finishTime;

    public static FiveEdOutReviewDto adapt(FiveEdOutReview item) {
        FiveEdOutReviewDto dto = new FiveEdOutReviewDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
