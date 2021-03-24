package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveEdReturnVisit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdReturnVisitDto extends FiveEdReturnVisit {
    private String processName;

   private String operateUserLogin;

    private String attendUser;


    private String finishTime;

    public static FiveEdReturnVisitDto adapt(FiveEdReturnVisit item) {
        FiveEdReturnVisitDto dto = new FiveEdReturnVisitDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
