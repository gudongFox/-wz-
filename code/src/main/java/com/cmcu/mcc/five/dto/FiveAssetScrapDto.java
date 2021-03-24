package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveAssetScrap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveAssetScrapDto extends FiveAssetScrap {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveAssetScrapDto adapt(FiveAssetScrap item) {
        FiveAssetScrapDto dto = new FiveAssetScrapDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
