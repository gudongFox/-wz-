package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdArrange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Setter
@Getter
public class FiveEdArrangeDto extends FiveEdArrange {
    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //是否默认项
    private Boolean defaultItem;

    public static FiveEdArrangeDto adapt(FiveEdArrange item) {
        FiveEdArrangeDto dto = new FiveEdArrangeDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
