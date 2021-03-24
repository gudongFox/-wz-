package com.cmcu.mcc.oa.dto;



import com.cmcu.mcc.oa.entity.OaMeetingRoomApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaMeetingRoomApplyDto extends OaMeetingRoomApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String meetingRoomName;

    public static OaMeetingRoomApplyDto adapt(OaMeetingRoomApply item) {
        OaMeetingRoomApplyDto dto=new OaMeetingRoomApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
