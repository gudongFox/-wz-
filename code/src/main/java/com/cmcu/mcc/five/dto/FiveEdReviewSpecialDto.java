package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdHouseRefer;
import com.cmcu.mcc.five.entity.FiveEdReviewSpecial;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveEdReviewSpecialDto extends FiveEdReviewSpecial {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdReviewSpecialDto adapt(FiveEdReviewSpecial item) {
        FiveEdReviewSpecialDto dto = new FiveEdReviewSpecialDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
