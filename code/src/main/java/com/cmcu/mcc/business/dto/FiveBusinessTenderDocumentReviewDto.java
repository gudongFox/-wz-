package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessTenderDocumentReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessTenderDocumentReviewDto extends FiveBusinessTenderDocumentReview {
    private String processName;

    private String operateUserLogin;

    public static FiveBusinessTenderDocumentReviewDto adapt(FiveBusinessTenderDocumentReview item) {
        FiveBusinessTenderDocumentReviewDto dto = new FiveBusinessTenderDocumentReviewDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
