package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.oa.dto.OaCarDto;
import com.cmcu.mcc.oa.entity.OaCar;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaCarDto extends FiveOaCar {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    public static FiveOaCarDto adapt(FiveOaCar item) {
        FiveOaCarDto dto=new FiveOaCarDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
