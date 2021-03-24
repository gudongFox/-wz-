package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaNonIndependentDeptSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaNonIndependentDeptSetDto extends FiveOaNonIndependentDeptSet {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaNonIndependentDeptSetDto adapt(FiveOaNonIndependentDeptSet item) {
        FiveOaNonIndependentDeptSetDto dto = new FiveOaNonIndependentDeptSetDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
