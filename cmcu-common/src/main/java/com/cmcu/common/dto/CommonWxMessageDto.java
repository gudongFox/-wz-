package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonWxMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CommonWxMessageDto extends CommonWxMessage {

    private String toUserName;


    public static CommonWxMessageDto adapt(CommonWxMessage item){
        CommonWxMessageDto dto=new CommonWxMessageDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
