package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrCertification;
import com.cmcu.mcc.hr.entity.HrQualify;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrCertificationDto extends HrCertification {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    public static HrCertificationDto adapt(HrCertification item){
        HrCertificationDto dto=new HrCertificationDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
