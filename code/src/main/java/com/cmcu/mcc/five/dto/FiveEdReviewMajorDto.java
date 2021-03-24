package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdReviewMajor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author hnz
 * @date 2019/10/18
 */
@Getter
@Setter
public class FiveEdReviewMajorDto extends FiveEdReviewMajor {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdReviewMajorDto adapt(FiveEdReviewMajor item) {
        FiveEdReviewMajorDto dto = new FiveEdReviewMajorDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
