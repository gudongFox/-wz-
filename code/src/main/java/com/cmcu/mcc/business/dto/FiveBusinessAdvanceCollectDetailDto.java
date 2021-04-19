package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollect;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollectDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessAdvanceCollectDetailDto extends FiveBusinessAdvanceCollectDetail {
    private String processName;

    private String operateUserLogin;

    public static FiveBusinessAdvanceCollectDetailDto adapt(FiveBusinessAdvanceCollectDetail item) {
        FiveBusinessAdvanceCollectDetailDto dto = new FiveBusinessAdvanceCollectDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
