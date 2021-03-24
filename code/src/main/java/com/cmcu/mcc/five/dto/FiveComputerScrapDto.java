package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveComputerScrap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveComputerScrapDto extends FiveComputerScrap {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveComputerScrapDto adapt(FiveComputerScrap item) {
        FiveComputerScrapDto dto = new FiveComputerScrapDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
