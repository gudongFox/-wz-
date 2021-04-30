package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonEdArrangeUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CommonEdArrangeUserDto extends CommonEdArrangeUser {

    private String buildName;

    private int buildId;

    private String majorName;

    private String allUserNameWithLogin;

    public static CommonEdArrangeUserDto adapt(CommonEdArrangeUser item){
        CommonEdArrangeUserDto dto=new CommonEdArrangeUserDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
