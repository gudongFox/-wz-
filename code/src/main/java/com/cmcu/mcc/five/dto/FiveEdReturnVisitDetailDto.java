package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdReturnVisitDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdReturnVisitDetailDto extends FiveEdReturnVisitDetail {

   private String operateUserLogin;

    private String attendUser;

    private String shortMarkContent;

    private String shortMarkAnswer;

    private Boolean cloudLocated;

    public static FiveEdReturnVisitDetailDto adapt(FiveEdReturnVisitDetail item){
        FiveEdReturnVisitDetailDto dto=new FiveEdReturnVisitDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
