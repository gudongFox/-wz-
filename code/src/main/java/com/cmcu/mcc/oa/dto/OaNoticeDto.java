package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaNotice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaNoticeDto extends OaNotice {
    private String processName;//流程名称

    private String operateUserLogin;//当前流程任务人

    private String finishTime;

    private Integer attachId;

    private boolean latest;

    public static OaNoticeDto adapt(OaNotice item) {
        OaNoticeDto dto=new OaNoticeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
