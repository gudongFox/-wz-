package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdReviewPlan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author hnz
 * @date 2019/10/18
 */
@Getter
@Setter
public class FiveEdReviewPlanDto extends FiveEdReviewPlan {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdReviewPlanDto adapt(FiveEdReviewPlan item) {
        FiveEdReviewPlanDto dto = new FiveEdReviewPlanDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
