package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaComputerNetwork;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveOaComputerNetworkDto extends FiveOaComputerNetwork {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaComputerNetworkDto adapt(FiveOaComputerNetwork item) {
        FiveOaComputerNetworkDto dto = new FiveOaComputerNetworkDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
