package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaMeetingRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaMeetingRoomDto extends OaMeetingRoom {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static OaMeetingRoomDto adapt(OaMeetingRoom item) {
        OaMeetingRoomDto dto=new OaMeetingRoomDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
