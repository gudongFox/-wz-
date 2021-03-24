package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceNodeDto extends FiveFinanceNode {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;


    public static FiveFinanceNodeDto adapt(FiveFinanceNode item) {
        FiveFinanceNodeDto dto=new FiveFinanceNodeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
