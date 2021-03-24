package com.cmcu.mcc.five.dto;



import com.cmcu.mcc.five.entity.FiveOaCarApply;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaCarMeetingRoomDto extends FiveOaMeetingRoomApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String carName;

    public static FiveOaCarMeetingRoomDto adapt(FiveOaMeetingRoomApply item) {
        FiveOaCarMeetingRoomDto dto=new FiveOaCarMeetingRoomDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
