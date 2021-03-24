package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdDesignDrawingCheck;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class FiveEdDesignDrawingCheckDto extends FiveEdDesignDrawingCheck {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private Boolean defaultItem;

    public static FiveEdDesignDrawingCheckDto adapt(FiveEdDesignDrawingCheck item) {
        FiveEdDesignDrawingCheckDto dto = new FiveEdDesignDrawingCheckDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
