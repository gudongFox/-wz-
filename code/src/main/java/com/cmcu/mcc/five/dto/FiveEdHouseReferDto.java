package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdHouseRefer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveEdHouseReferDto extends FiveEdHouseRefer {

    private List<String> myMajorNames;

    private String processName;

   private String operateUserLogin;

    private String attendUser;


    private String finishTime;

    private int fileCount;

    private String myTaskName;

    public static FiveEdHouseReferDto adapt(FiveEdHouseRefer item) {
        FiveEdHouseReferDto dto = new FiveEdHouseReferDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
