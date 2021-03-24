package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaDepartmentPost;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaDepartmentPostDto extends FiveOaDepartmentPost {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaDepartmentPostDto adapt(FiveOaDepartmentPost item) {
        FiveOaDepartmentPostDto dto = new FiveOaDepartmentPostDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
