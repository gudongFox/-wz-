package com.cmcu.mcc.ed.dto;

import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheck;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveEdMajorDrawingCheckDto extends FiveEdMajorDrawingCheck {

    private String processName;

    private String operateUserLogin;

    private String finishTime;


    public static FiveEdMajorDrawingCheckDto adapt(FiveEdMajorDrawingCheck item) {
        FiveEdMajorDrawingCheckDto dto = new FiveEdMajorDrawingCheckDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
