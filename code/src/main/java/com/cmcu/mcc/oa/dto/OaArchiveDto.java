package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaArchive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class OaArchiveDto extends OaArchive {

    private String processName;//流程名称

    private String operateUserLogin;//当前流程任务人

    private String businessKey;//关键字

    private String finishTime;

    public static OaArchiveDto adapt(OaArchive item) {
        OaArchiveDto dto=new OaArchiveDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
