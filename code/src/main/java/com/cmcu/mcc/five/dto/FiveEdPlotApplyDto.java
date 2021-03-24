package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveEdPlotApply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdPlotApplyDto extends FiveEdPlotApply {

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String businessKey;

    private String finishTime;

    private boolean selected;

    private int fileCount;

    public static FiveEdPlotApplyDto adapt(FiveEdPlotApply item) {
        FiveEdPlotApplyDto dto = new FiveEdPlotApplyDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

}
