package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaResearchProjectLibrary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaResearchProjectLibraryDto extends FiveOaResearchProjectLibrary {
    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static FiveOaResearchProjectLibraryDto adapt(FiveOaResearchProjectLibrary item) {
        FiveOaResearchProjectLibraryDto dto = new FiveOaResearchProjectLibraryDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
