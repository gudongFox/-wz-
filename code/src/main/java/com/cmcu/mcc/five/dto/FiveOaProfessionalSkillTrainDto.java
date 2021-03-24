package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainDeclare;
import com.cmcu.mcc.five.entity.FiveOaProfessionalSkillTrain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaProfessionalSkillTrainDto extends FiveOaProfessionalSkillTrain {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaProfessionalSkillTrainDto adapt(FiveOaProfessionalSkillTrain item) {
        FiveOaProfessionalSkillTrainDto dto = new FiveOaProfessionalSkillTrainDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
