package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaMeetingArrange;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FiveOaMeetingArrangeDto extends FiveOaMeetingArrange {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static FiveOaMeetingArrangeDto adapt(FiveOaMeetingArrange item) {
        FiveOaMeetingArrangeDto dto=new FiveOaMeetingArrangeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
