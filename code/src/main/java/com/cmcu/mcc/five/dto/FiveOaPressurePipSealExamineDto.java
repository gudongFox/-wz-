package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaPressurePipSealExamine;
import com.cmcu.mcc.five.entity.FiveOaTechnologyArticleExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaPressurePipSealExamineDto extends FiveOaPressurePipSealExamine {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaPressurePipSealExamineDto adapt(FiveOaPressurePipSealExamine item) {
        FiveOaPressurePipSealExamineDto dto = new FiveOaPressurePipSealExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
