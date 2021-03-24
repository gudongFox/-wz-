package com.cmcu.mcc.sys.dto;

import com.cmcu.mcc.sys.entity.SysSoftware;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author hnz
 * @date 2019/11/5
 */
@Getter
@Setter
public class SysSoftwareDto extends SysSoftware {

    private String sizeName;

    private String operateUserLogin;

    public static SysSoftwareDto adapt(SysSoftware dept){
        SysSoftwareDto dto=new SysSoftwareDto();
        BeanUtils.copyProperties(dept,dto);
        return dto;
    }
}
