package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrVacationDetail;
import org.springframework.beans.BeanUtils;

public class HrVacationDetailDto extends HrVacationDetail {

    public static HrVacationDetailDto adapt(HrVacationDetail item){
        HrVacationDetailDto dto=new HrVacationDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
