package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveAssetComputer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveAssetComputerDto extends FiveAssetComputer {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveAssetComputerDto adapt(FiveAssetComputer item) {
        FiveAssetComputerDto dto = new FiveAssetComputerDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
