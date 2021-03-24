package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaAssociationChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaAssociationChangeDto extends FiveOaAssociationChange {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaAssociationChangeDto adapt(FiveOaAssociationChange item) {
        FiveOaAssociationChangeDto dto = new FiveOaAssociationChangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
