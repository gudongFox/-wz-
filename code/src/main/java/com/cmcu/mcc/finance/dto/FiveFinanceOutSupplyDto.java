package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceOutSupply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceOutSupplyDto extends FiveFinanceOutSupply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceOutSupplyDto adapt(FiveFinanceOutSupply item) {
        FiveFinanceOutSupplyDto dto=new FiveFinanceOutSupplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
