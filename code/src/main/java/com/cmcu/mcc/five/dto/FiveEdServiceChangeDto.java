package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveEdServiceChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveEdServiceChangeDto extends FiveEdServiceChange {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private List<String> myMajorNames;

    public static FiveEdServiceChangeDto adapt(FiveEdServiceChange item) {
        FiveEdServiceChangeDto dto = new FiveEdServiceChangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
