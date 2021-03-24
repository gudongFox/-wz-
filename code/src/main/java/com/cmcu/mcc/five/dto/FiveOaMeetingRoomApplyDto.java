package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaMeetingRoomApplyDto extends FiveOaMeetingRoomApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String meetingRoomName;

    private String attendUser;

    private String applyState;

    public static FiveOaMeetingRoomApplyDto adapt(FiveOaMeetingRoomApply item) {
        FiveOaMeetingRoomApplyDto dto=new FiveOaMeetingRoomApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
