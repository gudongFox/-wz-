package com.cmcu.mcc.oa.dto;



import com.cmcu.mcc.oa.entity.OaCarApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaCarApplyDto extends OaCarApply {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private String carName;

    public static OaCarApplyDto adapt(OaCarApply item) {
        OaCarApplyDto dto=new OaCarApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
