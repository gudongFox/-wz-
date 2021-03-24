package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaSoftwareApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaSoftwareApplyDto extends OaSoftwareApply {
    private String processName;//流程名称

    private String operateUserLogin;//当前流程任务人

    private String businessKey;//关键字

    private String finishTime;

    public static OaSoftwareApplyDto adapt(OaSoftwareApply item) {
        OaSoftwareApplyDto dto=new OaSoftwareApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
