package com.cmcu.mcc.supervise.dto;

import com.cmcu.mcc.supervise.entity.FiveOaSuperviseFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSuperviseFileDto extends FiveOaSuperviseFile {
    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaSuperviseFileDto adapt(FiveOaSuperviseFile item) {
        FiveOaSuperviseFileDto dto = new FiveOaSuperviseFileDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
