package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdArrangePlan;
import com.cmcu.mcc.five.entity.FiveEdArrangeTimetable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveEdArrangeTimetableDto extends FiveEdArrangeTimetable {

    private List<String> planMajors;



    public static FiveEdArrangeTimetableDto adapt(FiveEdArrangeTimetable item) {
        FiveEdArrangeTimetableDto dto = new FiveEdArrangeTimetableDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
