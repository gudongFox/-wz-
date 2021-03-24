package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaStampApply;
import com.cmcu.mcc.oa.entity.OaTechnology;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaStampApplyDto extends OaStampApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaStampApplyDto adapt(OaStampApply item) {
        OaStampApplyDto dto=new OaStampApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
