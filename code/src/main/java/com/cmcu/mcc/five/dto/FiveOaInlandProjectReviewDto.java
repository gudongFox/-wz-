package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInlandProjectReviewDto extends FiveOaInlandProjectReview {

    private String processName;

    private String operateUserLogin;

    public static FiveOaInlandProjectReviewDto adapt(FiveOaInlandProjectReview item) {
        FiveOaInlandProjectReviewDto dto = new FiveOaInlandProjectReviewDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
