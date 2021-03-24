package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessOutAssist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBusinessOutAssistDto extends FiveBusinessOutAssist {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;

    public static FiveBusinessOutAssistDto adapt(FiveBusinessOutAssist item) {
        FiveBusinessOutAssistDto dto=new FiveBusinessOutAssistDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
