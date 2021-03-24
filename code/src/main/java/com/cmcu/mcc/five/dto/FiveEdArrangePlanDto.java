package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdArrange;
import com.cmcu.mcc.five.entity.FiveEdArrangePlan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
public class FiveEdArrangePlanDto extends FiveEdArrangePlan {

    private List<String> planValues;

    private List<String> planNames;

    private List<String> planMajors;

    public static FiveEdArrangePlanDto adapt(FiveEdArrangePlan item) {
        FiveEdArrangePlanDto dto = new FiveEdArrangePlanDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
