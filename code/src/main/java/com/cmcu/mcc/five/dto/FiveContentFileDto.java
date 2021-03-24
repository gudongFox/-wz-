package com.cmcu.mcc.five.dto;


import com.cmcu.mcc.five.entity.FiveContentFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveContentFileDto extends FiveContentFile {

    private String extensionName;//图标

    public static FiveContentFileDto adapt(FiveContentFile item) {
        FiveContentFileDto dto = new FiveContentFileDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
