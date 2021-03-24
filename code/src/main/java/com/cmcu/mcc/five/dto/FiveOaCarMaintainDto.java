package com.cmcu.mcc.five.dto;



import com.cmcu.mcc.five.entity.FiveOaCarApply;
import com.cmcu.mcc.five.entity.FiveOaCarMaintain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaCarMaintainDto extends FiveOaCarMaintain {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String carName;

    private String attendUser;

    public static FiveOaCarMaintainDto adapt(FiveOaCarMaintain item) {
        FiveOaCarMaintainDto dto=new FiveOaCarMaintainDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
