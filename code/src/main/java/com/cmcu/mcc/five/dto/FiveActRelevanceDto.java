package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveActRelevance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveActRelevanceDto extends FiveActRelevance {

    public static FiveActRelevanceDto adapt(FiveActRelevance item) {
        FiveActRelevanceDto dto = new FiveActRelevanceDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
