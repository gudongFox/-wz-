package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDecisionMaking;
import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDecisionMakingDetailDto extends FiveOaDecisionMarkingDetail {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaDecisionMakingDetailDto adapt(FiveOaDecisionMarkingDetail item) {
        FiveOaDecisionMakingDetailDto dto = new FiveOaDecisionMakingDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
