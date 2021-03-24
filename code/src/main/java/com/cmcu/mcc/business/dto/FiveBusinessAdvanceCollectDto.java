package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessAdvance;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollect;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessAdvanceCollectDto extends FiveBusinessAdvanceCollect {
    private String processName;

    private String operateUserLogin;

    public static FiveBusinessAdvanceCollectDto adapt(FiveBusinessAdvanceCollect item) {
        FiveBusinessAdvanceCollectDto dto = new FiveBusinessAdvanceCollectDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
