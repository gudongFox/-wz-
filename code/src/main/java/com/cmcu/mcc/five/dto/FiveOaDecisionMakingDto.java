package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDecisionMaking;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDecisionMakingDto extends FiveOaDecisionMaking {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private int makingNum;//上会议题数

    public static FiveOaDecisionMakingDto adapt(FiveOaDecisionMaking item) {
        FiveOaDecisionMakingDto dto = new FiveOaDecisionMakingDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
