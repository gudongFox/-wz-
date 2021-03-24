package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaStampApplyOffice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaStampApplyOfficeDto extends FiveOaStampApplyOffice {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaStampApplyOfficeDto adapt(FiveOaStampApplyOffice item) {
        FiveOaStampApplyOfficeDto dto = new FiveOaStampApplyOfficeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

}
