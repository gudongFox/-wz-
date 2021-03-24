package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaTechnologyArticleExamine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaTechnologyArticleExamineDto extends FiveOaTechnologyArticleExamine {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaTechnologyArticleExamineDto adapt(FiveOaTechnologyArticleExamine item) {
        FiveOaTechnologyArticleExamineDto dto = new FiveOaTechnologyArticleExamineDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
