package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrInsure;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrInsureDto extends HrInsure {
    private String operateUserLogin;

    private String processName;

    private String businessKey;

    private String finishTime;

    public static HrInsureDto adapt(HrInsure item){
        HrInsureDto dto=new HrInsureDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
