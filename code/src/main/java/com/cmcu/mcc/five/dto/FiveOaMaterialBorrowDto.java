package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaMaterialBorrow;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaMaterialBorrowDto extends FiveOaMaterialBorrow {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaMaterialBorrowDto adapt(FiveOaMaterialBorrow item) {
        FiveOaMaterialBorrowDto dto = new FiveOaMaterialBorrowDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
