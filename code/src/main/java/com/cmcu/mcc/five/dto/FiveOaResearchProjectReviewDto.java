package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaResearchProjectReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaResearchProjectReviewDto extends FiveOaResearchProjectReview {
    private String processName;

    private String operateUserLogin;

    public static FiveOaResearchProjectReviewDto adapt(FiveOaResearchProjectReview item) {
        FiveOaResearchProjectReviewDto dto = new FiveOaResearchProjectReviewDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
