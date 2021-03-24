package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdReviewApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveEdReviewApplyDto extends FiveEdReviewApply {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveEdReviewApplyDto adapt(FiveEdReviewApply item) {
        FiveEdReviewApplyDto dto = new FiveEdReviewApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
