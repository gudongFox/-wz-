package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrPosition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrPositionDto extends HrPosition {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    private String childDeptIdNames;

    public static HrPositionDto adapt(HrPosition item){
        HrPositionDto dto=new HrPositionDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
