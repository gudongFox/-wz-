package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class FiveHrQualifyChiefDto extends FiveHrQualifyChief {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private HrEmployeeDto hrEmployeeDto;

    public static FiveHrQualifyChiefDto adapt(FiveHrQualifyChief item) {
        FiveHrQualifyChiefDto dto = new FiveHrQualifyChiefDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}