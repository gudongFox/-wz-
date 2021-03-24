package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaAssociationApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaAssociationApplyDto extends FiveOaAssociationApply {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaAssociationApplyDto adapt(FiveOaAssociationApply item) {
        FiveOaAssociationApplyDto dto = new FiveOaAssociationApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
