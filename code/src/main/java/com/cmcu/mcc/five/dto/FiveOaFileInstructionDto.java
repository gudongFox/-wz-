package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaFileInstruction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaFileInstructionDto extends FiveOaFileInstruction {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaFileInstructionDto adapt(FiveOaFileInstruction item) {
        FiveOaFileInstructionDto dto = new FiveOaFileInstructionDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }



}
