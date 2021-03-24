package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessOutAssist;
import com.cmcu.mcc.business.entity.FiveBusinessOutAssistDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBusinessOutAssistDetailDto extends FiveBusinessOutAssistDetail {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    public static FiveBusinessOutAssistDetailDto adapt(FiveBusinessOutAssistDetail item) {
        FiveBusinessOutAssistDetailDto dto=new FiveBusinessOutAssistDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
