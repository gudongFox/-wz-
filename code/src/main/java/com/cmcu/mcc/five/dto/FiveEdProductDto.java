package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdProduct;
import com.cmcu.mcc.five.entity.FiveEdStamp;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdProductDto  extends FiveEdProduct {

    private String processName;

   private String operateUserLogin;

    private String attendUser;


    public static FiveEdProductDto adapt(FiveEdProduct item) {
        FiveEdProductDto dto = new FiveEdProductDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
