package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveHrQualifyApply;
import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class FiveHrQualifyApplyDto extends FiveHrQualifyApply {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private long userCount;

    private String userLoginList;

    private HrEmployeeDto hrEmployeeDto;

    public static FiveHrQualifyApplyDto adapt(FiveHrQualifyApply item) {
        FiveHrQualifyApplyDto dto = new FiveHrQualifyApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}