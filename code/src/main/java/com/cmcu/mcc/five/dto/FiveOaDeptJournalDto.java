package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDeptJournal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDeptJournalDto extends FiveOaDeptJournal {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaDeptJournalDto adapt(FiveOaDeptJournal item) {
        FiveOaDeptJournalDto dto = new FiveOaDeptJournalDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
