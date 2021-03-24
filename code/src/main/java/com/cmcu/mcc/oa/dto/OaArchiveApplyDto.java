package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaArchiveApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class OaArchiveApplyDto extends OaArchiveApply {

    private String processName;//流程名称

    private String operateUserLogin;//当前流程任务人

    private String businessKey;//关键字

    private String finishTime;

    private String projectName;//项目名称

    public static OaArchiveApplyDto adapt(OaArchiveApply item) {
        OaArchiveApplyDto dto=new OaArchiveApplyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
