package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaCarApply;
import com.cmcu.mcc.oa.entity.OaNoticeApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaNoticeApplyDto extends OaNoticeApply {
    private String processName;

    private String operateUserLogin;

    private String finishTime;

    public static OaNoticeApplyDto adapt(OaNoticeApply item) {
        OaNoticeApplyDto dto=new OaNoticeApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
