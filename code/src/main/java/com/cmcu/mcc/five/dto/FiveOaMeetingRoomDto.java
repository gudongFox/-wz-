package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FiveOaMeetingRoomDto extends FiveOaMeetingRoom {
    private String processName;

    private String operateUserLogin;

    //查询当前的被使用时段
    private List<Map> usedTimes;

    public static FiveOaMeetingRoomDto adapt(FiveOaMeetingRoom item) {
        FiveOaMeetingRoomDto dto=new FiveOaMeetingRoomDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
