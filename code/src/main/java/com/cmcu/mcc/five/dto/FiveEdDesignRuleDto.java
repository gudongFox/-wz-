package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdDesignRule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdDesignRuleDto extends FiveEdDesignRule {
    private String processName;

  private String operateUserLogin;

    private String attendUser;

    private String businessKey;

    private String finishTime;

    public static FiveEdDesignRuleDto adapt(FiveEdDesignRule item) {
        FiveEdDesignRuleDto dto = new FiveEdDesignRuleDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
