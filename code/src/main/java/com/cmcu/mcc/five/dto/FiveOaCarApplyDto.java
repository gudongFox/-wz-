package com.cmcu.mcc.five.dto;



import com.cmcu.mcc.five.entity.FiveOaCarApply;
import com.cmcu.mcc.oa.dto.OaCarApplyDto;
import com.cmcu.mcc.oa.entity.OaCarApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaCarApplyDto extends FiveOaCarApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String carName;

    private String attendUser;

    public static FiveOaCarApplyDto adapt(FiveOaCarApply item) {
        FiveOaCarApplyDto dto=new FiveOaCarApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
