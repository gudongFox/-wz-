package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessContractReview;
import com.cmcu.mcc.five.entity.FiveOaReviewContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessContractReviewDto extends FiveBusinessContractReview {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private int headDeptId;

    public static FiveBusinessContractReviewDto adapt(FiveBusinessContractReview item) {
        FiveBusinessContractReviewDto dto = new FiveBusinessContractReviewDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
