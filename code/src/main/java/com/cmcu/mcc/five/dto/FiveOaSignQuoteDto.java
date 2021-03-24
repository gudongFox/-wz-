package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaSignQuoteDto extends FiveOaSignQuote {
    private String processName;
    private String operateUserLogin;
    private String attendUser;
    private String finishTime;

    public static FiveOaSignQuoteDto adapt(FiveOaSignQuote item) {
        FiveOaSignQuoteDto dto = new FiveOaSignQuoteDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
