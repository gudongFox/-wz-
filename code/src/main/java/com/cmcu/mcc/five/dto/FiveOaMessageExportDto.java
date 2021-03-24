package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaMessageExport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaMessageExportDto extends FiveOaMessageExport {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaMessageExportDto adapt(FiveOaMessageExport item) {
        FiveOaMessageExportDto dto = new FiveOaMessageExportDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
