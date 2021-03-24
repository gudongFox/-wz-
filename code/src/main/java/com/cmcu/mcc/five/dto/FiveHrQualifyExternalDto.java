package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.five.entity.FiveHrQualifyExternal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author hnz
 * @date 2019/10/29
 */
@Getter
@Setter
public class FiveHrQualifyExternalDto  extends FiveHrQualifyExternal {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    public static FiveHrQualifyExternalDto adapt(FiveHrQualifyExternal item) {
        FiveHrQualifyExternalDto dto = new FiveHrQualifyExternalDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
