package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceBackLetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceBackLetterDto extends FiveFinanceBackLetter {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveFinanceBackLetterDto adapt(FiveFinanceBackLetter item) {
        FiveFinanceBackLetterDto dto = new FiveFinanceBackLetterDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
