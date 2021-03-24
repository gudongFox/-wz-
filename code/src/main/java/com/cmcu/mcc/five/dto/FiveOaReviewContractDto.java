package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaReviewContract;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaReviewContractDto extends FiveOaReviewContract {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaReviewContractDto adapt(FiveOaReviewContract item) {
        FiveOaReviewContractDto dto = new FiveOaReviewContractDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
