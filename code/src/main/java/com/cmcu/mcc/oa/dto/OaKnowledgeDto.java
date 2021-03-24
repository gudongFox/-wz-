package com.cmcu.mcc.oa.dto;


import com.cmcu.mcc.oa.entity.OaKnowledge;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class OaKnowledgeDto extends OaKnowledge {
    private String processName;//流程名称

    private String operateUserLogin;//当前流程任务人

    private String finishTime;

    public static OaKnowledgeDto adapt(OaKnowledge item) {
        OaKnowledgeDto dto=new OaKnowledgeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
