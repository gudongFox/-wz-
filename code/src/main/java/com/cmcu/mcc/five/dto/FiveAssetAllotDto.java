package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveAssetAllot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveAssetAllotDto extends FiveAssetAllot {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveAssetAllotDto adapt(FiveAssetAllot item) {
        FiveAssetAllotDto dto = new FiveAssetAllotDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
